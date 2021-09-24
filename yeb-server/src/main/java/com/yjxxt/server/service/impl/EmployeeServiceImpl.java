package com.yjxxt.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjxxt.server.pojo.Employee;
import com.yjxxt.server.mapper.EmployeeMapper;
import com.yjxxt.server.pojo.RespBean;
import com.yjxxt.server.pojo.RespPageBean;
import com.yjxxt.server.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tk
 * @since 2021-09-24
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;

    @Override
    public RespPageBean getEmployeeByPage(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope) {
        //开启分页
        Page<Employee> page=new Page<>(currentPage,size);
        IPage<Employee> employeeIPage= employeeMapper.getEmployeeByPage(page,employee,beginDateScope);
        RespPageBean respPageBean=new
                RespPageBean(employeeIPage.getTotal(),employeeIPage.getRecords());
        return respPageBean;
    }

    /**
     * 获取工号
     * @return
     */
    @Override
    public RespBean maxWorkId() {
        List<Map<String,Object>> maps=employeeMapper.selectMaps(new QueryWrapper<Employee>().select("max(workId)"));
        return RespBean.success(null,String.format("%08d",Integer.parseInt(maps.get(0).get("max(workId)").toString())+1));
    }

    /**
     * 添加员工
     * @param employee
     * @return
     */
    @Override
    public RespBean insertEmployee(Employee employee) {
        //处理合同期限，保留2位小数
        LocalDate beginContract=employee.getBeginContract();
        LocalDate endContract = employee.getEndContract();
        long days=beginContract.until(endContract, ChronoUnit.DAYS);
        //==>long until(Temporal endExclusive, TemporalUnit unit);
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(days/365.00)));
        if(1==employeeMapper.insert(employee)){
            return RespBean.success("添加成功！");
        }
        return RespBean.error("添加失败！");
    }
}
