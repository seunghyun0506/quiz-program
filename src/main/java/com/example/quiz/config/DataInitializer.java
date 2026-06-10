package com.example.quiz.config;

import com.example.quiz.domain.Question;
import com.example.quiz.domain.QuestionOption;
import com.example.quiz.domain.Quiz;
import com.example.quiz.domain.User;
import com.example.quiz.dto.UserRegisterRequestDTO;
import com.example.quiz.repository.QuizRepository;
import com.example.quiz.repository.UserRepository;
import com.example.quiz.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final UserService userService;

    public DataInitializer(UserRepository userRepository, QuizRepository quizRepository, UserService userService) {
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. Seed Default User if not exists
        if (userRepository.findByEmail("test@test.com").isEmpty()) {
            UserRegisterRequestDTO testUserDTO = new UserRegisterRequestDTO(
                "test@test.com",
                "password123",
                "퀴즈마스터"
            );
            userService.register(testUserDTO);
        }

        // 2. Seed Default Quiz if not exists
        if (quizRepository.findAll().isEmpty()) {
            User creator = userRepository.findByEmail("test@test.com").get();

            Quiz quiz = new Quiz(
                "Java 기초 및 핵심 개념 퀴즈",
                "Java 프로그래밍 언어의 필수 기초 상식과 핵심 메모리 구조, 기본 문법을 검증하는 종합 테스트 퀴즈 세트입니다.",
                "Java",
                creator,
                LocalDateTime.now()
            );

            // Question 1: Multiple choice (selection limit = 2)
            Question q1 = new Question(
                "다음 중 Java의 기본 데이터 타입(Primitive Type)을 모두 고르시오.",
                2,
                30,
                "int와 boolean은 Java의 기본 데이터 타입(Primitive Type)입니다. 반면 String과 Integer는 참조 타입(Reference Type)이자 클래스 객체입니다."
            );
            q1.addOption(new QuestionOption("int", true));
            q1.addOption(new QuestionOption("String", false));
            q1.addOption(new QuestionOption("boolean", true));
            q1.addOption(new QuestionOption("Integer", false));
            quiz.addQuestion(q1);

            // Question 2: OX quiz (selection limit = 1)
            Question q2 = new Question(
                "Java에서 모든 사용자 정의 클래스는 상속 관계를 정의하지 않더라도 명시적/암묵적으로 Object 클래스를 상속받는다.",
                1,
                15,
                "Java의 모든 클래스는 상속이 없을 시 암묵적으로 java.lang.Object 클래스를 최상위 슈퍼클래스로 상속받게 설계되어 있습니다."
            );
            q2.addOption(new QuestionOption("참 (O)", true));
            q2.addOption(new QuestionOption("거짓 (X)", false));
            quiz.addQuestion(q2);

            // Question 3: Multiple choice (selection limit = 1)
            Question q3 = new Question(
                "JVM 메모리 구조 중, 인스턴스화된 객체(new 키워드로 생성된 객체)가 저장되고 가비지 컬렉션(GC)이 일어나는 전용 영역은 어디인가요?",
                1,
                20,
                "Heap 영역은 프로그램이 실행되는 도중 동적으로 생성되는 객체(인스턴스)들이 적재되는 영역이며 가비지 컬렉션의 대상이 됩니다. Stack은 지역변수, Method Area는 클래스 정보와 static 멤버들이 보관됩니다."
            );
            q3.addOption(new QuestionOption("Stack Area", false));
            q3.addOption(new QuestionOption("Method Area (Metaspace)", false));
            q3.addOption(new QuestionOption("Heap Area", true));
            q3.addOption(new QuestionOption("Native Method Stack", false));
            quiz.addQuestion(q3);

            quizRepository.save(quiz);
        }
    }
}
