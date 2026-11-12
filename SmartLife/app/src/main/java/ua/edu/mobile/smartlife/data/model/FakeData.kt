
package ua.edu.mobile.smartlife.data.model

object FakeData {

    val records = listOf(
        HealthRecord(
            id = 1L,
            type = RecordType.WATER,
            value = 0.5,
            note = "Після пробіжки",
            dateTime = "23.09.2026 12:09"
        ),
        HealthRecord(
            id = 2L,
            type = RecordType.PULSE,
            value = 84.0,
            note = "У стані спокою",
            dateTime = "23.09.2026 11:09"
        ),
        HealthRecord(
            id = 3L,
            type = RecordType.SLEEP,
            value = 7.5,
            note = "Якісний сон",
            dateTime = "23.09.2026 06:09"
        ),
        HealthRecord(
            id = 4L,
            type = RecordType.WEIGHT,
            value = 55.2,
            note = "Ранкове вимірювання",
            dateTime = "23.09.2026 07:00"
        )
    )
}
