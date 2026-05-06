package org.example.project

import com.russhwolf.settings.Settings

private val settings: Settings = Settings()

object AppSettings {

    var darkTheme: Boolean
        get() = settings.getBoolean(key = "dark_theme", defaultValue = false)
        set(value) { settings.putBoolean(key = "dark_theme", value = value) }

    var useWeekSchedule: Boolean
        get() = settings.getBoolean(key = "use_week_schedule", defaultValue = false)
        set(value) { settings.putBoolean(key = "use_week_schedule", value = value) }
}