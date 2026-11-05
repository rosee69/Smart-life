
package ua.edu.mobile.smartlife.ui.navigation

import kotlinx.serialization.Serializable

@Serializable object LoginRoute
@Serializable object RegisterRoute
@Serializable object HomeRoute
@Serializable object RecordsRoute
@Serializable data class RecordDetailsRoute(val recordId: Long)
@Serializable object MapRoute
@Serializable object ProfileRoute
@Serializable object StatisticsRoute
@Serializable object DeviceRoute
@Serializable object SettingsRoute
@Serializable object BleRoute
@Serializable object LocationRoute
@Serializable object SensorsRoute
