<h1> 1. Status bar theme-ing: </h1>
Just use StatusBar(lightMode) or StatusBar(darkMode) inside any screen composable to change the statusBar theme on that screen.



<h1> 2. DriftTime API Documentation </h1> 


## 1. Getting a DriftDate Instance (The Entry Points)

Everything starts with the `Time` object. 
You use this to grab a `DriftDate` instance, which is the engine that handles all the formatting and extractions.

```kotlin
// Get the exact current date and time
val currentDate = Time.now

// Get a date from a specific Unix timestamp (milliseconds)
val pastDate = Time.from(1686826310000L) 
```

## 2. Extracting Raw Numbers

Once you have a `DriftDate`, you can easily pull out individual integer values without messing with `Calendar` objects.
Note that the `month` property intelligently adjusts the 0-indexed Java calendar to a standard 1-12 format. 

```kotlin
val date = Time.now
val year = date.year       // e.g., 2026
val month = date.month     // e.g., 6 (for June)
val day = date.day         // e.g., 15
val hour12 = date.hour12   // e.g., 11 (12-hour format)
val hour24 = date.hour24   // e.g., 23 (24-hour format)
val minute = date.minute   // e.g., 5
```

## 3. Extracting Text Representations

If you need the names of days or months for UI elements, `DriftDate` provides these as simple String properties.

```kotlin
val date = Time.now
val dayName = date.dayName             // "Monday"
val shortDay = date.dayNameShort       // "Mon"
val monthName = date.monthName         // "June"
val shortMonth = date.monthNameShort   // "Jun"
val amPmMarker = date.amPm             // "AM" or "PM"
```

## 4. Pre-built Formatted Strings (The Clever Part)

You utilized Kotlin's `operator fun invoke()` for the `timeString` and `fullString` properties. This means you can call the property like a function to get the default format, or access its nested property to get the 24-hour format.

**Time Strings:**

```kotlin
val date = Time.now

// DEFAULT (12-hour): Notice the parentheses! This triggers the invoke() function.
val defaultTime = date.timeString() // Output: "11:05 AM"

// 24-HOUR: Accessed as a property inside timeString.
val militaryTime = date.timeString.to24HourFormat // Output: "11:05" (or "23:05" if at night)
```

**Date Strings:**

```kotlin
val dateOnly = date.dateString // Output: "15 Jun 2026"
```

**Full Strings (Date + Time):** 

```kotlin
// DEFAULT (12-hour): Requires parentheses
val full12 = date.fullString() // Output: "Monday, 15 Jun 2026 • 11:05 AM"

// 24-HOUR: Property access
val full24 = date.fullString.to24HourFormat // Output: "Monday, 15 Jun 2026 • 11:05"
```

> **Important Note for Documentation:** Emphasize to your team that `timeString()` and `fullString()` require the `()` brackets to get the default string, otherwise, they will just print the reference to the Provider object itself!

## 5. Smart Checks

These are boolean flags perfect for chat interfaces, feed timestamps, or notification groupings.

```kotlin
val date = Time.now

if (date.isToday) {
    println("This happened today!")
}

if (date.isYesterday) {
    println("This happened yesterday.")
}
```

## 6. Custom Formatting

If the pre-built strings don't fit a specific design requirement, you have a fallback method to pass in any standard `SimpleDateFormat` pattern. 

```kotlin
val date = Time.now

// Pass any valid SimpleDateFormat string
val customStr = date.format("yyyy/MM/dd HH:mm:ss") // Output: "2026/06/15 11:05:10"
```
