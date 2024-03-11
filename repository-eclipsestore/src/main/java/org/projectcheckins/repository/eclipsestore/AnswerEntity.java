package org.projectcheckins.repository.eclipsestore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.projectcheckins.core.forms.Format;

import java.time.LocalDate;

public class AnswerEntity {

  @NotBlank
  private String id;

  private Format format;

  @NotBlank
  private String questionId;

  @NotBlank
  private String userId;

  @NotNull
  @PastOrPresent
  private LocalDate answerDate;

  @NotBlank
  private String text;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getQuestionId() {
    return questionId;
  }

  public void setQuestionId(String questionId) {
    this.questionId = questionId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public LocalDate getAnswerDate() {
    return answerDate;
  }

  public void setAnswerDate(LocalDate answerDate) {
    this.answerDate = answerDate;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }
}
