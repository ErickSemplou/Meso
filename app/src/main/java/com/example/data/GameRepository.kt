package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.City
import com.example.model.DiplomaticStatus
import com.example.model.FactionRelation
import com.example.model.GameState
import com.example.model.PlayerResources
import com.example.model.TradeRoute
import org.json.JSONArray
import org.json.JSONObject

data class SavedGameSummary(
    val factionId: String,
    val turn: Int,
    val yearBCE: Int,
    val citiesCount: Int
)

class GameRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("mesopotamia_save_prefs", Context.MODE_PRIVATE)

    fun hasSavedGame(): Boolean {
        return prefs.getBoolean("has_saved_campaign", false)
    }

    fun getSavedGameSummary(): SavedGameSummary? {
        if (!hasSavedGame()) return null
        val jsonStr = prefs.getString("campaign_data_v1", null) ?: return null
        return try {
            val root = JSONObject(jsonStr)
            val factionId = root.optString("playerFactionId", "uruk")
            val turn = root.optInt("turn", 1)
            val yearBCE = root.optInt("yearBCE", 2600)
            val citiesArray = root.optJSONArray("cities")
            var count = 0
            if (citiesArray != null) {
                for (i in 0 until citiesArray.length()) {
                    val c = citiesArray.getJSONObject(i)
                    if (c.optString("factionId") == factionId) count++
                }
            }
            SavedGameSummary(factionId, turn, yearBCE, count.coerceAtLeast(1))
        } catch (_: Exception) {
            null
        }
    }

    fun saveGame(state: GameState) {
        try {
            val root = JSONObject()
            root.put("turn", state.turn)
            root.put("yearBCE", state.yearBCE)
            root.put("playerFactionId", state.playerFactionId)
            root.put("isVictory", state.isVictory)
            root.put("isDefeat", state.isDefeat)
            root.put("currentTechId", state.currentTechId ?: "")
            root.put("currentTechTurnsRemaining", state.currentTechTurnsRemaining)

            // Resources
            val resObj = JSONObject()
            resObj.put("grain", state.resources.grain)
            resObj.put("clay", state.resources.clay)
            resObj.put("bronze", state.resources.bronze)
            resObj.put("silver", state.resources.silver)
            resObj.put("loyalty", state.resources.loyalty)
            resObj.put("piety", state.resources.piety)
            resObj.put("maxGrainStorage", state.resources.maxGrainStorage)
            root.put("resources", resObj)

            // Cities
            val citiesArray = JSONArray()
            state.cities.forEach { city ->
                val cObj = JSONObject()
                cObj.put("id", city.id)
                cObj.put("name", city.name)
                cObj.put("title", city.title)
                cObj.put("factionId", city.factionId)
                cObj.put("mapX", city.mapX.toDouble())
                cObj.put("mapY", city.mapY.toDouble())
                cObj.put("river", city.river)
                cObj.put("population", city.population)
                cObj.put("loyalty", city.loyalty)
                cObj.put("uniqueTrait", city.uniqueTrait)
                cObj.put("traitDescription", city.traitDescription)
                cObj.put("bonusGrain", city.bonusGrain)
                cObj.put("bonusClay", city.bonusClay)
                cObj.put("bonusBronze", city.bonusBronze)
                cObj.put("bonusSilver", city.bonusSilver)
                cObj.put("bonusDefense", city.bonusDefense)
                cObj.put("buildingInProgress", city.buildingInProgress ?: "")
                cObj.put("buildingTurnsRemaining", city.buildingTurnsRemaining)
                cObj.put("historyFact", city.historyFact)

                val bldArray = JSONArray()
                city.buildings.forEach { bldArray.put(it) }
                cObj.put("buildings", bldArray)

                val garrObj = JSONObject()
                city.garrison.forEach { (unitId, count) -> garrObj.put(unitId, count) }
                cObj.put("garrison", garrObj)

                val tpArray = JSONArray()
                city.tradePartners.forEach { tpArray.put(it) }
                cObj.put("tradePartners", tpArray)

                citiesArray.put(cObj)
            }
            root.put("cities", citiesArray)

            // Relations
            val relArray = JSONArray()
            state.relations.forEach { (facId, rel) ->
                val rObj = JSONObject()
                rObj.put("factionId", facId)
                rObj.put("status", rel.status.name)
                rObj.put("relationshipScore", rel.relationshipScore)
                rObj.put("turnsOfPeace", rel.turnsOfPeace)
                rObj.put("tributePaidByUs", rel.tributePaidByUs)
                rObj.put("tributePaidToUs", rel.tributePaidToUs)
                relArray.put(rObj)
            }
            root.put("relations", relArray)

            // Trade Routes
            val tradeArray = JSONArray()
            state.tradeRoutes.forEach { tr ->
                val trObj = JSONObject()
                trObj.put("id", tr.id)
                trObj.put("originCityId", tr.originCityId)
                trObj.put("destinationCityId", tr.destinationCityId)
                trObj.put("exportedResource", tr.exportedResource)
                trObj.put("importedResource", tr.importedResource)
                trObj.put("turnsActive", tr.turnsActive)
                trObj.put("profitPerTurn", tr.profitPerTurn)
                tradeArray.put(trObj)
            }
            root.put("tradeRoutes", tradeArray)

            // Researched Techs
            val techsArray = JSONArray()
            state.researchedTechIds.forEach { techsArray.put(it) }
            root.put("researchedTechIds", techsArray)

            // Chronicle
            val chronArray = JSONArray()
            state.chronicleLog.takeLast(30).forEach { chronArray.put(it) }
            root.put("chronicleLog", chronArray)

            prefs.edit()
                .putString("campaign_data_v1", root.toString())
                .putBoolean("has_saved_campaign", true)
                .apply()
        } catch (_: Exception) {
            // Fail safe
        }
    }

    fun loadGame(): GameState? {
        val jsonStr = prefs.getString("campaign_data_v1", null) ?: return null
        return try {
            val root = JSONObject(jsonStr)
            val turn = root.getInt("turn")
            val yearBCE = root.getInt("yearBCE")
            val playerFactionId = root.getString("playerFactionId")
            val isVictory = root.optBoolean("isVictory", false)
            val isDefeat = root.optBoolean("isDefeat", false)
            val currentTechId = root.optString("currentTechId").takeIf { it.isNotEmpty() }
            val currentTechTurnsRemaining = root.optInt("currentTechTurnsRemaining", 0)

            // Resources
            val resObj = root.getJSONObject("resources")
            val resources = PlayerResources(
                grain = resObj.optInt("grain", 150),
                clay = resObj.optInt("clay", 120),
                bronze = resObj.optInt("bronze", 40),
                silver = resObj.optInt("silver", 200),
                loyalty = resObj.optInt("loyalty", 85),
                piety = resObj.optInt("piety", 50),
                maxGrainStorage = resObj.optInt("maxGrainStorage", 400)
            )

            // Cities
            val cities = mutableListOf<City>()
            val citiesArray = root.getJSONArray("cities")
            for (i in 0 until citiesArray.length()) {
                val cObj = citiesArray.getJSONObject(i)
                val id = cObj.getString("id")
                val name = cObj.getString("name")
                val title = cObj.getString("title")
                val factionId = cObj.getString("factionId")
                val mapX = cObj.getDouble("mapX").toFloat()
                val mapY = cObj.getDouble("mapY").toFloat()
                val river = cObj.getString("river")
                val population = cObj.getInt("population")
                val loyalty = cObj.optInt("loyalty", 80)
                val uniqueTrait = cObj.optString("uniqueTrait", "Місто Бронзової доби")
                val traitDescription = cObj.optString("traitDescription", "")
                val bonusGrain = cObj.optInt("bonusGrain", 0)
                val bonusClay = cObj.optInt("bonusClay", 0)
                val bonusBronze = cObj.optInt("bonusBronze", 0)
                val bonusSilver = cObj.optInt("bonusSilver", 0)
                val bonusDefense = cObj.optInt("bonusDefense", 0)
                val buildingInProgress = cObj.optString("buildingInProgress").takeIf { it.isNotEmpty() }
                val buildingTurnsRemaining = cObj.optInt("buildingTurnsRemaining", 0)
                val historyFact = cObj.getString("historyFact")

                val bldList = mutableListOf<String>()
                val bldArray = cObj.getJSONArray("buildings")
                for (j in 0 until bldArray.length()) {
                    bldList.add(bldArray.getString(j))
                }

                val garrison = mutableMapOf<String, Int>()
                val garrObj = cObj.getJSONObject("garrison")
                val keys = garrObj.keys()
                while (keys.hasNext()) {
                    val k = keys.next()
                    garrison[k] = garrObj.getInt(k)
                }

                val tradePartners = mutableListOf<String>()
                val tpArray = cObj.optJSONArray("tradePartners")
                if (tpArray != null) {
                    for (j in 0 until tpArray.length()) {
                        tradePartners.add(tpArray.getString(j))
                    }
                }

                cities.add(
                    City(
                        id = id,
                        name = name,
                        title = title,
                        factionId = factionId,
                        mapX = mapX,
                        mapY = mapY,
                        river = river,
                        population = population,
                        uniqueTrait = uniqueTrait,
                        traitDescription = traitDescription,
                        bonusGrain = bonusGrain,
                        bonusClay = bonusClay,
                        bonusBronze = bonusBronze,
                        bonusSilver = bonusSilver,
                        bonusDefense = bonusDefense,
                        buildings = bldList,
                        buildingInProgress = buildingInProgress,
                        buildingTurnsRemaining = buildingTurnsRemaining,
                        garrison = garrison,
                        loyalty = loyalty,
                        tradePartners = tradePartners,
                        historyFact = historyFact
                    )
                )
            }

            // Relations
            val relations = mutableMapOf<String, FactionRelation>()
            val relArray = root.getJSONArray("relations")
            for (i in 0 until relArray.length()) {
                val rObj = relArray.getJSONObject(i)
                val fId = rObj.getString("factionId")
                val statusStr = rObj.getString("status")
                val status = try {
                    DiplomaticStatus.valueOf(statusStr)
                } catch (_: Exception) {
                    DiplomaticStatus.NEUTRAL
                }
                val score = rObj.getInt("relationshipScore")
                relations[fId] = FactionRelation(
                    factionId = fId,
                    status = status,
                    relationshipScore = score,
                    turnsOfPeace = rObj.optInt("turnsOfPeace", 0),
                    tributePaidByUs = rObj.optInt("tributePaidByUs", 0),
                    tributePaidToUs = rObj.optInt("tributePaidToUs", 0)
                )
            }

            // Trade routes
            val tradeRoutes = mutableListOf<TradeRoute>()
            val trArray = root.getJSONArray("tradeRoutes")
            for (i in 0 until trArray.length()) {
                val trObj = trArray.getJSONObject(i)
                tradeRoutes.add(
                    TradeRoute(
                        id = trObj.getString("id"),
                        originCityId = trObj.getString("originCityId"),
                        destinationCityId = trObj.getString("destinationCityId"),
                        exportedResource = trObj.getString("exportedResource"),
                        importedResource = trObj.getString("importedResource"),
                        turnsActive = trObj.getInt("turnsActive"),
                        profitPerTurn = trObj.getInt("profitPerTurn")
                    )
                )
            }

            // Techs
            val techs = mutableSetOf<String>()
            val techArray = root.getJSONArray("researchedTechIds")
            for (i in 0 until techArray.length()) {
                techs.add(techArray.getString(i))
            }

            // Decrees
            val decrees = mutableSetOf<String>()
            val decrArray = root.optJSONArray("activeDecreeIds")
            if (decrArray != null) {
                for (i in 0 until decrArray.length()) {
                    decrees.add(decrArray.getString(i))
                }
            }

            // Chronicle
            val chronicle = mutableListOf<String>()
            val chronArray = root.optJSONArray("chronicleLog")
            if (chronArray != null) {
                for (i in 0 until chronArray.length()) {
                    chronicle.add(chronArray.getString(i))
                }
            }

            GameState(
                turn = turn,
                yearBCE = yearBCE,
                playerFactionId = playerFactionId,
                resources = resources,
                cities = cities,
                relations = relations,
                tradeRoutes = tradeRoutes,
                researchedTechIds = techs,
                currentTechId = currentTechId,
                currentTechTurnsRemaining = currentTechTurnsRemaining,
                chronicleLog = chronicle,
                isVictory = isVictory,
                isDefeat = isDefeat
            )
        } catch (e: Exception) {
            null
        }
    }

    fun clearSavedGame() {
        prefs.edit().clear().apply()
    }
}
