package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.Faction
import com.example.model.GameState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun readStringFromContext() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Месопотамія", appName)
  }

  @Test
  fun testInitialGameState() {
    val state = GameState.createInitial("uruk")
    assertEquals("uruk", state.playerFactionId)
    assertEquals("Урук", state.playerFaction.name)
    assertEquals(2600, state.yearBCE)
    assertEquals(1, state.turn)
    assertNotNull(state.playerCities.find { it.id == "uruk" })
  }
}
