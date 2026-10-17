package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.CurriculumData
import com.example.model.CharacterRepository
import com.example.model.Language
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Vidya AI", appName)
    }

    @Test
    fun `verify curriculum topics and characters loaded`() {
        val topics = CurriculumData.allTopics
        assertTrue("Curriculum must have topics", topics.isNotEmpty())

        val characters = CharacterRepository.allCharacters
        assertEquals(3, characters.size)

        // Verify multilingual strings
        val topic = topics.first()
        val titleEn = topic.title.get(Language.ENGLISH)
        val titleTe = topic.title.get(Language.TELUGU)
        val titleHi = topic.title.get(Language.HINDI)

        assertTrue(titleEn.isNotBlank())
        assertTrue(titleTe.isNotBlank())
        assertTrue(titleHi.isNotBlank())
    }
}
