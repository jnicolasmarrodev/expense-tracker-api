package com.nico.expensetracker.service;

import com.nico.expensetracker.dto.ExpenseRequestDTO;
import com.nico.expensetracker.dto.ExpenseResponseDTO;
import com.nico.expensetracker.dto.ExpenseUpdateRequestDTO;
import com.nico.expensetracker.entity.Expense;
import com.nico.expensetracker.entity.User;
import com.nico.expensetracker.exception.ExpenseNotFoundException;
import com.nico.expensetracker.mapper.ExpenseMapper;
import com.nico.expensetracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserService userService;

    public ExpenseService(
            ExpenseRepository expenseRepository,
            UserService userService
    ) {
        this.expenseRepository = expenseRepository;
        this.userService = userService;
    }

    public ExpenseResponseDTO create(ExpenseRequestDTO dto) {

        User user = userService.findByIdOrThrow(dto.userId());

        Expense expense = ExpenseMapper.toEntity(dto, user);

        Expense savedExpense = expenseRepository.save(expense);

        return ExpenseMapper.toResponse(savedExpense);
    }

    public ExpenseResponseDTO findById(Long id){

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException(id));

        return ExpenseMapper.toResponse(expense);
    }

    public List<ExpenseResponseDTO> findAll() {

        return expenseRepository.findAll()
                .stream()
                .map(ExpenseMapper::toResponse)
                .toList();
    }

    public void delete(Long id) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException(id));

        expenseRepository.delete(expense);
    }

    public ExpenseResponseDTO update(
            Long id,
            ExpenseUpdateRequestDTO dto
    ) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException(id));

        expense.update(dto);

        Expense updatedExpense = expenseRepository.save(expense);

        return ExpenseMapper.toResponse(updatedExpense);
    }

}
