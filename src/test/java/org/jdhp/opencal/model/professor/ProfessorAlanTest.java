package org.jdhp.opencal.model.professor;

import java.util.ArrayList;
import java.util.List;

import org.jdhp.opencal.model.card.Card;
import org.jdhp.opencal.model.card.Review;
import org.jdhp.opencal.model.professor.Professor;
import org.jdhp.opencal.model.professor.ProfessorFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfessorAlanTest {

    private Professor professor;
    
    @BeforeEach
    void init() throws Exception {
        this.professor = ProfessorFactory.createProfessor("Alan");
    }

    @Test
    void testAssess() {
        /*
         * testArray[][0] = cdate         (ex: "2008-01-01")
         * testArray[][1] = reviewDates   (ex: "2008-01-02=good,2008-01-04=good")
         * testArray[][2] = expectedGrade (ex: "2.0")
         */
        String testArray[][] = {
                {"2008-01-01", "2008-01-02=good,2008-01-04=good", "2.0"},
                {"2008-01-01", "2008-01-02=good,2008-01-04=bad,2008-01-05=good", "1.0"},
                {"2008-01-01", "2008-01-02=good,2008-01-04=bad", "0.0"}};
        
        for(String[] test : testArray) {
            Card card = this.createCard(test[0], test[1]);
            assertEquals(Float.parseFloat(test[2]), card.getGrade());
        }
    }
    
    Card createCard(String creationDate, String reviewsString) {
        
        // reviews
        String reviewArray[] = reviewsString.split(",");
        List<Review> reviewList = new ArrayList<Review>();
        
        for(String reviewString : reviewArray) {
            String reviewAttributes[] = reviewString.split("=");
            reviewList.add(new Review(reviewAttributes[0], reviewAttributes[1]));
        }
        
        Card card = new Card("test", "test", new ArrayList<String>(), reviewList, creationDate, false);
        card.setGrade(this.professor.assess(card));
        
        return card;
    }

}
