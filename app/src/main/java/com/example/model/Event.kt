package com.example.model

data class GameEventOption(
    val text: String,
    val description: String,
    val grainChange: Int = 0,
    val clayChange: Int = 0,
    val bronzeChange: Int = 0,
    val silverChange: Int = 0,
    val loyaltyChange: Int = 0,
    val pietyChange: Int = 0,
    val outcomeMessage: String
)

data class GameEvent(
    val id: String,
    val title: String,
    val historicalContext: String,
    val description: String,
    val options: List<GameEventOption>
) {
    companion object {
        val ALL_EVENTS = listOf(
            GameEvent(
                id = "flood_euphrates",
                title = "Великий розлив Євфрату",
                historicalContext = "Тигр і Євфрат розливалися непередбачувано навесні, коли в горах танули сніги. Шумери вірили, що це гнів бога вод Енкі.",
                description = "Бурхливі каламутні води несуть мул і загрожують змити греблі та затопити посіви ячменю на навколишніх полях. Що накаже володар?",
                options = listOf(
                    GameEventOption(
                        text = "Мобілізувати ополчення на зміцнення дамб",
                        description = "Витратити зерно на пайки робітникам та глину на ремонт насипів.",
                        grainChange = -25,
                        clayChange = -30,
                        loyaltyChange = +10,
                        outcomeMessage = "Дамби встояли! Вода слухняно пішла по зрошувальних ровах, удобривши ґрунт родючим мулом."
                    ),
                    GameEventOption(
                        text = "Принести щедрі жертви богу Енкі у храмі",
                        description = "Жерці проведуть священні молебні з пахощами та сріблом.",
                        silverChange = -30,
                        pietyChange = +25,
                        grainChange = -15,
                        outcomeMessage = "Боги вгамували бурю. Народ переконаний у священній силі вашого правління!"
                    )
                )
            ),
            GameEvent(
                id = "nomad_martu_raid",
                title = "Набіг степовиків Марту",
                historicalContext = "Кочівники Марту (амореї) жили в західних пустелях. Шумери писали про них: «Вони не знають зерна, не ховають померлих і блукають степами».",
                description = "Вартові помітили хмару куряви на обрії. Швидкі загони кочівників на віслюках підійшли до приміських сіл і вимагають здобичі.",
                options = listOf(
                    GameEventOption(
                        text = "Вислати фалангу та лучників на перехоплення",
                        description = "Дати бій загарбникам під стінами міста.",
                        bronzeChange = +15,
                        silverChange = +25,
                        loyaltyChange = +15,
                        outcomeMessage = "Блискуча перемога! Стрільці розсіяли кочівників, а воїни захопили трофейну бронзу та стада худоби."
                    ),
                    GameEventOption(
                        text = "Укласти мирний договір і обдарувати вождів зерном",
                        description = "Зберегти життя містян і найняти кочівників на захист кордонів.",
                        grainChange = -35,
                        silverChange = -20,
                        loyaltyChange = +5,
                        outcomeMessage = "Вождь кочівників прийняв дари і пообіцяв охороняти караванні шляхи від інших розбійників."
                    )
                )
            ),
            GameEvent(
                id = "dilmun_traders",
                title = "Караван із загадкового Дільмуна",
                historicalContext = "Острів Дільмун (сучасний Бахрейн) вважався шумерським «раєм», де не було хвороб і смерті. Звідти везли мідь, перли та деревину.",
                description = "В очеретяний порт прибули дивовижні вітрильники. Купці привезли чисту олов'яну руду, слонову кістку та лазурит.",
                options = listOf(
                    GameEventOption(
                        text = "Викупити всю руду до царських майстерень",
                        description = "Витратити срібло для оснащення армії новою бронзою.",
                        silverChange = -45,
                        bronzeChange = +35,
                        outcomeMessage = "Ковалі розпалили горна! Армія отримала десятки новеньких бронзових шоломів та сокир."
                    ),
                    GameEventOption(
                        text = "Обміняти надлишок царського зерна та вовни",
                        description = "Вигідний бартерний обмін без витрати скарбниці.",
                        grainChange = -30,
                        bronzeChange = +20,
                        pietyChange = +10,
                        outcomeMessage = "Торговці задоволені шумерським ячменем, а храм прикрашено привезеним лазуритом!"
                    )
                )
            ),
            GameEvent(
                id = "drought_heatwave",
                title = "Пекельна посуха",
                historicalContext = "Улітку температура в Месопотамії досягає +50°C. Без води з каналів зелені поля перетворювалися на випалену пустелю за лічені дні.",
                description = "Рівень води в річці критично впав. Сонце випалює поля, селяни скаржаться на нестачу хліба і погрожують відмовитися від роботи.",
                options = listOf(
                    GameEventOption(
                        text = "Відчинити царські комори і роздати зерно людям",
                        description = "Врятувати містян від голоду за рахунок запасів.",
                        grainChange = -40,
                        loyaltyChange = +25,
                        outcomeMessage = "Люди прославляють вашого правителя на всіх базарах! Лояльність міст сягнула небес."
                    ),
                    GameEventOption(
                        text = "Зберегти зерно в резерві та провести молебень",
                        description = "Зберегти продовольчу безпеку армії на випадок війни.",
                        grainChange = 0,
                        loyaltyChange = -10,
                        pietyChange = +15,
                        outcomeMessage = "Скарбниця і зерно цілі, але серед простолюду чутно глухе ремствування."
                    )
                )
            )
        )
    }
}
