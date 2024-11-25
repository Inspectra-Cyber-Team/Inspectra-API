package co.istad.inspectra.base;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseSpecification<T> {

    public Specification<T> filter(SpecsDto specsDto)
        {
            return new Specification<T>() {
                @Override
                public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.like(root.get(specsDto.getColumn()), "%" + specsDto.getValue() + "%");
                }
            };
        }

    @Setter
    @Getter
    public static class SpecsDto {

        private String column;
        private String value;

        private Operation operation;

        public enum Operation{
            EQUAL, LIKE, IN, GREATER_THAN, LESS_THAN, BETWEEN, JOIN;
        }
    }


    @Setter
    @Getter
    public static class FilterDto {

        private List<SpecsDto> specsDto;

        private GlobalOperator globalOperator;

        public enum GlobalOperator{
            AND, OR;
        }
    }



}