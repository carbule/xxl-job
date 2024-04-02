package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.constants.RedisConstant;
import com.korant.youya.workplace.enums.TransactionFlowStatusEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.SysCommissionRecordMapper;
import com.korant.youya.workplace.mapper.SysWalletAccountMapper;
import com.korant.youya.workplace.mapper.WalletTransactionFlowMapper;
import com.korant.youya.workplace.pojo.po.SysCommissionRecord;
import com.korant.youya.workplace.pojo.po.SysWalletAccount;
import com.korant.youya.workplace.pojo.po.WalletTransactionFlow;
import com.korant.youya.workplace.service.SysCommissionRecordService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 友涯抽成记录表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2024-02-22
 */
@Service
@Slf4j
public class SysCommissionRecordServiceImpl extends ServiceImpl<SysCommissionRecordMapper, SysCommissionRecord> implements SysCommissionRecordService {

    @Resource
    private SysCommissionRecordMapper sysCommissionRecordMapper;

    @Resource
    private SysWalletAccountMapper sysWalletAccountMapper;

    @Resource
    private WalletTransactionFlowMapper walletTransactionFlowMapper;

    @Resource
    private RedissonClient redissonClient;

    /**
     * 平台奖金抽成
     *
     * @param commissionId
     */
    @Override
    public void commissionPayment(Long commissionId) {
        log.info("开始执行平台抽成");
        SysCommissionRecord sysCommissionRecord = sysCommissionRecordMapper.selectOne(new LambdaQueryWrapper<SysCommissionRecord>().eq(SysCommissionRecord::getId, commissionId).eq(SysCommissionRecord::getIsDelete, 0));
        if (null == sysCommissionRecord) {
            log.info("平台抽成id：【{}】，未找到对应记录，停止抽成", commissionId);
            return;
        }
        String commissionOrderId = sysCommissionRecord.getCommissionOrderId();
        WalletTransactionFlow walletTransactionFlow = walletTransactionFlowMapper.selectOne(new LambdaQueryWrapper<WalletTransactionFlow>().eq(WalletTransactionFlow::getOrderId, commissionOrderId).eq(WalletTransactionFlow::getIsDelete, 0));
        if (null == walletTransactionFlow) {
            log.info("平台抽成id：【{}】，未找到对应交易流水记录，停止抽成", commissionId);
            return;
        }
        Long accountId = sysCommissionRecord.getAccountId();
        String walletLockKey = String.format(RedisConstant.YY_WALLET_ACCOUNT_LOCK, accountId);
        RLock walletLock = redissonClient.getLock(walletLockKey);
        try {
            boolean tryWalletLock = walletLock.tryLock(3, TimeUnit.SECONDS);
            if (tryWalletLock) {
                SysWalletAccount sysWalletAccount = sysWalletAccountMapper.selectOne(new LambdaQueryWrapper<SysWalletAccount>().eq(SysWalletAccount::getId, accountId).eq(SysWalletAccount::getIsDelete, 0));
                if (null == sysWalletAccount) {
                    log.info("平台抽成id：【{}】，未找到系统钱包账户，停止抽成", commissionId);
                    return;
                }
                BigDecimal commissionAmount = sysCommissionRecord.getCommissionAmount();
                BigDecimal availableBalance = sysWalletAccount.getAvailableBalance();
                BigDecimal amount = availableBalance.add(commissionAmount);
                sysWalletAccount.setAvailableBalance(amount);
                sysWalletAccountMapper.updateById(sysWalletAccount);
                walletTransactionFlow.setStatus(TransactionFlowStatusEnum.SUCCESSFUL.getStatus());
                walletTransactionFlow.setTradeStatusDesc(TransactionFlowStatusEnum.SUCCESSFUL.getStatusDesc());
                walletTransactionFlow.setBalanceBefore(availableBalance);
                walletTransactionFlow.setBalanceAfter(amount);
                walletTransactionFlow.setCompletionDate(LocalDateTime.now());
                walletTransactionFlowMapper.updateById(walletTransactionFlow);
            } else {
                log.error("平台抽成id：【{}】，获取钱包账户锁超时", commissionId);
                throw new YouyaException("获取钱包账户锁超时");
            }
        } catch (InterruptedException e) {
            log.error("平台抽成id：【{}】，获取钱包账户锁失败，原因：", commissionId, e);
            throw new YouyaException("获取钱包账户锁失败");
        } finally {
            if (walletLock != null && walletLock.isHeldByCurrentThread()) {
                walletLock.unlock();
            }
        }
    }
}
