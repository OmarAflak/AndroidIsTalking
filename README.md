# Android is Talking

Simple "is talking" sound wave animation.

![random](https://github.com/OmarAflak/AndroidIsTalking/blob/master/gif/random.gif)
![sin](https://github.com/OmarAflak/AndroidIsTalking/blob/master/gif/sin.gif)

## Usage

```xml
<me.aflak.libraries.AndroidIsTalking
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/is_talking"
    android:layout_width="wrap_content"
    android:layout_height="15dp"
    app:barCount="5"
    app:barWidth="2dp"
    app:barSpace="1dp"
    app:barColor="#4287f5"
    app:animationDuration="200" />
```

...

```kotlin
val isTalking = findViewById<AndroidIsTalking>(R.id.is_talking)
isTalking.start()
isTalking.stop()
```

## States

The library animates the heights of the bars based on a list of "states". Each state consist of a list of numbers between `0f` and `1f` representing the percentage of height taken by the bars.

For example, `listOf(1f, 1f, 1f, 0.5f)` means `3` bars at full height, and `1` bar at half height.

The states are provided by an interface called `StateProvider`. The library has two defaults: `RandomStateProvider` and `SinStateProvider`.

You can implement your own state provider as follows:

```kotlin
object MyStateProvider : StateProvider {
    override fun getStates(size: Int): List<List<Float>> {
        /**
         * size: Size of each state (number of bars)
         */
        TODO("Return a list of states to go through")
    }
}
```

Check out the implementation of [SinStateProvider.kt](https://github.com/OmarAflak/AndroidIsTalking/blob/master/androidistalking/src/main/java/me/aflak/libraries/SinStateProvider.kt) !

Finally, use the provider:

```kotlin
val isTalking = findViewById<AndroidIsTalking>(R.id.is_talking)
isTalking.stateProvider = MyStateProvider
```
