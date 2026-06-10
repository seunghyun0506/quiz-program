package com.example.quiz;

import com.example.quiz.domain.Question;
import com.example.quiz.domain.QuestionOption;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class QuizGradingTests {

    @Test
    void testGradingSingleChoiceCorrect() {
        Question question = new Question("Capital of France?", 1, 10, "Paris is capital.");
        QuestionOption o1 = new QuestionOption("London", false); o1.setId(101L);
        QuestionOption o2 = new QuestionOption("Paris", true); o2.setId(102L);
        QuestionOption o3 = new QuestionOption("Berlin", false); o3.setId(103L);
        question.addOption(o1);
        question.addOption(o2);
        question.addOption(o3);

        // Grade correct
        List<Long> correctOptionIds = Arrays.asList(102L);
        List<Long> selected = Arrays.asList(102L);
        boolean isCorrect = selected.size() == correctOptionIds.size()
            && selected.containsAll(correctOptionIds)
            && correctOptionIds.containsAll(selected);
        assertTrue(isCorrect);

        // Grade incorrect
        List<Long> selectedWrong = Arrays.asList(101L);
        boolean isCorrectWrong = selectedWrong.size() == correctOptionIds.size()
            && selectedWrong.containsAll(correctOptionIds)
            && correctOptionIds.containsAll(selectedWrong);
        assertFalse(isCorrectWrong);
    }

    @Test
    void testGradingMultipleChoiceCorrect() {
        Question question = new Question("Select primitive types:", 2, 20, "int and char are primitives.");
        QuestionOption o1 = new QuestionOption("int", true); o1.setId(1L);
        QuestionOption o2 = new QuestionOption("String", false); o2.setId(2L);
        QuestionOption o3 = new QuestionOption("char", true); o3.setId(3L);
        question.addOption(o1);
        question.addOption(o2);
        question.addOption(o3);

        List<Long> correctOptionIds = Arrays.asList(1L, 3L);

        // Correct selection (order independent)
        List<Long> selectedCorrect = Arrays.asList(3L, 1L);
        boolean isCorrect = selectedCorrect.size() == correctOptionIds.size()
            && selectedCorrect.containsAll(correctOptionIds)
            && correctOptionIds.containsAll(selectedCorrect);
        assertTrue(isCorrect);

        // Partial selection (incorrect)
        List<Long> selectedPartial = Arrays.asList(1L);
        boolean isCorrectPartial = selectedPartial.size() == correctOptionIds.size()
            && selectedPartial.containsAll(correctOptionIds)
            && correctOptionIds.containsAll(selectedPartial);
        assertFalse(isCorrectPartial);

        // Over-selection (incorrect)
        List<Long> selectedOver = Arrays.asList(1L, 2L, 3L);
        boolean isCorrectOver = selectedOver.size() == correctOptionIds.size()
            && selectedOver.containsAll(correctOptionIds)
            && correctOptionIds.containsAll(selectedOver);
        assertFalse(isCorrectOver);
    }
}
