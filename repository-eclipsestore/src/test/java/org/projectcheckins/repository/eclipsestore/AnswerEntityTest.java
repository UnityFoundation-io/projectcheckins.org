package org.projectcheckins.repository.eclipsestore;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.security.authentication.Authentication;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.projectcheckins.core.forms.AnswerSave;

class AnswerEntityTest {

  @Test
  void testAnswerEntityFromAnswerSave() {
    final String questionId = "xxx";
    final String answerId = "yyy";
    final LocalDate answerDate = LocalDate.now();
    final String text = "Lorem ipsum";
    final String userId = "Admin";
    final AnswerSave answerSave = new AnswerSave(questionId, answerDate, text);
    final Authentication auth = Authentication.build(userId, emptyList(), emptyMap());
    final AnswerEntity entity = AnswerEntity.fromAnswer(answerId, answerSave, auth);
    assertThat(entity)
        .hasFieldOrPropertyWithValue("id", answerId)
        .hasFieldOrPropertyWithValue("questionId", questionId)
        .hasFieldOrPropertyWithValue("userId", userId)
        .hasFieldOrPropertyWithValue("answerDate", answerDate)
        .hasFieldOrPropertyWithValue("text", text);
  }
}
