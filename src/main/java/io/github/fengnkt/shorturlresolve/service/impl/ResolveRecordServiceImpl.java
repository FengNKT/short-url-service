package io.github.fengnkt.shorturlresolve.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.fengnkt.shorturlresolve.dto.Result;
import io.github.fengnkt.shorturlresolve.dto.StatsDTO;
import io.github.fengnkt.shorturlresolve.entity.ResolveRecord;
import io.github.fengnkt.shorturlresolve.mapper.ResolveRecordMapper;
import io.github.fengnkt.shorturlresolve.service.IResolveRecordService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ResolveRecordServiceImpl extends ServiceImpl<ResolveRecordMapper, ResolveRecord> implements IResolveRecordService {

    @Override
    public Result statsByShortCode(String shortCode) {
        List<ResolveRecord> stats = query().eq("short_code", shortCode).list();
        StatsDTO statsDTO = new StatsDTO();
        statsDTO.setShortCode(shortCode);
        statsDTO.setFrequency(stats.size());
        return Result.ok(statsDTO);
    }
}
