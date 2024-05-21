package com.weather.service.repositoryImpl;

import com.weather.common.model.ExcelData;
import com.weather.service.repository.ExcelDataRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

/*
    TODO ExcelDataRepositoryCustom -> 커스텀 메소드를 호출해서 모든 데이터를 처리할 수 있도록 하기 위해
        쿼리를 커스텀 한다.
 */

public class ExcelDataRepositoryImpl implements ExcelDataRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ExcelData> findAllExcelData() {
        String jpql = "SELECT e FROM ExcelData e";
        TypedQuery<ExcelData> query = entityManager.createQuery(jpql, ExcelData.class);
        return query.getResultList();
    }

    // TODO DB 데이터 검색
    @Override
    public List<ExcelData> searchByLocation(String query) {
        String[] searchTerms = query.split("\\s+");
        StringBuilder jpql = new StringBuilder("SELECT e FROM ExcelData e WHERE ");
        List<String> conditions = new ArrayList<>();

        for (String term : searchTerms) {
            conditions.add("(e.firstLevel LIKE :term OR e.secondLevel LIKE :term OR e.thirdLevel LIKE :term)");
        }

        jpql.append(String.join(" AND ", conditions));

        TypedQuery<ExcelData> typedQuery = entityManager.createQuery(jpql.toString(), ExcelData.class);

        for (String term : searchTerms) {
            typedQuery.setParameter("term", "%" + term + "%");
        }

        return typedQuery.getResultList();
    }

    // TODO 검색결과 페이지 처리
    @Override
    public Page<ExcelData> searchByLocationPaged(String query, Pageable pageable) {
        String[] searchTerms = query.split("\\s+");
        StringBuilder jpql = new StringBuilder("SELECT e FROM ExcelData e WHERE ");
        List<String> conditions = new ArrayList<>();

        for (String term : searchTerms) {
            conditions.add("(e.firstLevel LIKE :term OR e.secondLevel LIKE :term OR e.thirdLevel LIKE :term)");
        }

        jpql.append(String.join(" AND ", conditions));

        TypedQuery<ExcelData> typedQuery = entityManager.createQuery(jpql.toString(), ExcelData.class);

        for (String term : searchTerms) {
            typedQuery.setParameter("term", "%" + term + "%");
        }

        int totalRows = typedQuery.getResultList().size();
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(typedQuery.getResultList(), pageable, totalRows);
    }
}
