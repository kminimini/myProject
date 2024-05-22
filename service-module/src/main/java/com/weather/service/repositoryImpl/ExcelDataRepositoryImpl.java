package com.weather.service.repositoryImpl;

import com.weather.common.model.ExcelData;
import com.weather.service.repository.ExcelDataRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/*
    TODO ExcelDataRepositoryCustom -> 커스텀 메소드를 호출해서 모든 데이터를 처리할 수 있도록 하기 위해
        쿼리를 커스텀 한다.
 */

public class ExcelDataRepositoryImpl implements ExcelDataRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    // TODO ExcelData -> 리스트로 반환
    @Override
    public List<ExcelData> findAllExcelData() {
        String jpql = "SELECT e FROM ExcelData e";
        TypedQuery<ExcelData> query = entityManager.createQuery(jpql, ExcelData.class);
        return query.getResultList();
    }

    // TODO 검색결과 페이지 처리
    @Override
    public Page<ExcelData> findByLocationContaining(String query, Pageable pageable) {
        String jpql = "SELECT e FROM ExcelData e WHERE e.firstLevel LIKE :query OR e.secondLevel LIKE :query OR e.thirdLevel LIKE :query";
        TypedQuery<ExcelData> typedQuery = entityManager.createQuery(jpql, ExcelData.class);
        typedQuery.setParameter("query", "%" + query + "%");

        int totalRows = typedQuery.getResultList().size();
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<ExcelData> resultList = typedQuery.getResultList();
        return new PageImpl<>(resultList, pageable, totalRows);
    }
}
