package hexlet.code.specification;

import hexlet.code.dto.TaskParamsDTO;
import hexlet.code.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TaskSpecification {
    public Specification<Task> build(TaskParamsDTO params) {
        return withAssigneeId(params.getAssigneeId())
                .and(withLabelId(params.getLabelId()))
                .and(withTitleCont(params.getTitleCont()))
                .and(withStatus(params.getStatus()));
    }

    private Specification<Task> withLabelId(Set<Long> labelId) {
        return (root, query, cb) ->
                labelId == null ? cb.conjunction() : root.join("labels").get("id").in(labelId);
    }

    private Specification<Task> withAssigneeId(Long assigneeId) {
        return (root, query, cb) ->
                assigneeId == null ? cb.conjunction() : cb.equal(root.get("assignee").get("id"), assigneeId);
    }

    public static Specification<Task> withTitleCont(String title) {
        return (root, query, cb) ->
                title == null ? cb.conjunction() : cb.like(cb.lower(root.get("name")),
                        ("%" + title.toLowerCase() + "%"));
    }

    public static Specification<Task> withStatus(String status) {
        return (root, query, cb) ->
                status == null ? cb.conjunction() : cb.like(cb.lower(root.get("taskStatus").get("slug")),
                        ("%" + status.toLowerCase() + "%"));
    }
}
