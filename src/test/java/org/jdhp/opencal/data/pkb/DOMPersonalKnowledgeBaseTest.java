package org.jdhp.opencal.data.pkb;

import org.jdhp.opencal.data.pkb.DOMPersonalKnowledgeBase;

import org.jdhp.opencal.model.card.Card;
import org.jdhp.opencal.model.cardcollection.CardCollection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

class DOMPersonalKnowledgeBaseTest {

    /**
     * Default DOM parser puts some unicode caracters like 𝔓 or 𝓔 outside the CDATA section, e.g.:
     *     <![CDATA[The following caracter ]]>𝓔<![CDATA[ is put outside the cdata section with DOM parser.]]>
     * instead of
     *     <![CDATA[The following caracter 𝓔 is put outside the cdata section with DOM parser.]]>
     *
     * This test ensures that both versions gives the same results with DOMPersonalKnowledgeBase.
     */
    @Test
    @DisplayName("Test Unicode inside CDATA == Unicode outside CDATA")
    void xmlCDataUnicodeTest() throws PersonalKnowledgeBaseException {
        //// See https://howtoprogram.xyz/2017/01/17/read-file-and-resource-in-junit-test/
        //File file = new File("src/test/resources/unicode.pkb");
        //assertTrue(file.exists());

        // See https://howtoprogram.xyz/2017/01/17/read-file-and-resource-in-junit-test/
        ClassLoader classLoader = this.getClass().getClassLoader();
        File file = new File(classLoader.getResource("xml_cdata_unicode.pkb").getFile());
        assertTrue(file.exists());

        PersonalKnowledgeBase pkbInstance = new DOMPersonalKnowledgeBase();
        CardCollection cardCollection = pkbInstance.load(file.toURI());
        for(Card card : cardCollection) {
            assertEquals(card.getQuestion(), card.getAnswer());
        }
    }

}
