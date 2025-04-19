package com.example.soundarchive.dao.impl;

import com.example.soundarchive.dao.ArtistDAO;
import com.example.soundarchive.exception.DataNotFoundException;
import com.example.soundarchive.exception.InternalServerErrorException;
import com.example.soundarchive.model.dto.pagination.ListPagedResultDTO;
import com.example.soundarchive.model.entity.ArtistEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArtistDAOImpl implements ArtistDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ArtistEntity findById(Integer id) {

        ArtistEntity artistEntity;

        try {

            artistEntity = entityManager.find(ArtistEntity.class, id);

            if(artistEntity == null) throw new DataNotFoundException("Artist with id: " + id + " not found.");

        } catch(DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        return artistEntity;
    }

    @Override
    public List<ArtistEntity> findByName(String name) {

        List<ArtistEntity> artistEntities;

//        if(name.equals(":name")) {
//            artistEntities = findAll();
//
//            return artistEntities;
//        }

        try {
            String queryStr = "FROM ArtistEntity WHERE lower(artistName) LIKE lower(concat('%', :name, '%'))";
            TypedQuery<ArtistEntity> query = entityManager.createQuery(queryStr, ArtistEntity.class);
            query.setParameter("name", name);
            artistEntities = query.getResultList();

        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        return artistEntities;
    }

    @Override
    public ListPagedResultDTO<ArtistEntity> findAll(Pageable pageable, String name) {

        int total;
        List<ArtistEntity> artistEntities;

        try {
            StringBuilder countQueryStr = new StringBuilder("SELECT COUNT(a) FROM ArtistEntity a");
            countQueryStr.append(" WHERE 1=1");
            if (name != null) {
                countQueryStr.append(" AND lower(a.artistName) LIKE lower(concat('%', :name, '%'))");
            }

            TypedQuery<Long> countQuery = entityManager.createQuery(countQueryStr.toString(), Long.class);

            if (name != null) {
                countQuery.setParameter("name", name);
            }

            total = countQuery.getSingleResult().intValue();



            StringBuilder queryStr = new StringBuilder("SELECT a FROM ArtistEntity a");
            queryStr.append(" WHERE 1=1");
            if (name != null && !name.isEmpty()) {
                queryStr.append(" AND lower(a.artistName) LIKE lower(concat('%', :name, '%'))");
            }

            if (pageable.getSort().isSorted()) {
                for (Sort.Order order : pageable.getSort()) {
                    if (!order.getProperty().equals("artistName")) {
                        throw new InternalServerErrorException("Available sort value is only artistName. " +
                                "ArtistDAOImpl, findAll method.");
                    }
                    queryStr.append(" ORDER BY a.").append(order.getProperty()).append(" ").append(order.getDirection().name());
                }
            }

            TypedQuery<ArtistEntity> query = entityManager.createQuery(queryStr.toString(), ArtistEntity.class);

            if (name != null) {
                query.setParameter("name", name);
            }

            query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());

            artistEntities = query.getResultList();


            if(artistEntities.isEmpty()) throw new DataNotFoundException("No artists found.");

        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        return new ListPagedResultDTO<>(total, artistEntities);
    }

    @Transactional
    @Override
    public ArtistEntity save(ArtistEntity artistEntity) {

        try {
            entityManager.persist(artistEntity);

        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        return artistEntity;
    }

    @Transactional
    @Override
    public ArtistEntity update(ArtistEntity artistEntity) {

        try {
            entityManager.merge(artistEntity);

        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        return artistEntity;
    }

    @Transactional
    @Override
    public void delete(Integer id) {

        try {

            ArtistEntity artistEntity = entityManager.find(ArtistEntity.class, id);

            if (artistEntity != null) {
                entityManager.remove(artistEntity);
            }
            else {
                throw new DataNotFoundException("Artist with id: " + id + " not found.");
            }

        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        }catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }
    }
}
