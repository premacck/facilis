# Facilis

Swipe gesture based navigational library for Android.

[Sample app on Play Store here](https://play.google.com/store/apps/details?id=com.prembros.facilis)

## Getting Started

To get this project into your build:

### Gradle

Add it in your root build.gradle at the end of repositories:
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Add the dependency to your module's build.gradle
```
dependencies {
	implementation 'com.github.premacck:facilis:1.0.0'
}
```

### Maven

```
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```
Add the dependency
```
<dependency>
    <groupId>com.github.premacck</groupId>
    <artifactId>facilis</artifactId>
    <version>1.0.0</version>
</dependency>
```

### SBT
Add it in your build.sbt at the end of resolvers:
```
resolvers += "jitpack" at "https://jitpack.io"
```
Add the dependency
```
libraryDependencies += "com.github.premacck" % "facilis" % "1.0.0"
```

### Leiningen
Add it in your project.clj at the end of repositories:
```
:repositories [["jitpack" "https://jitpack.io"]]
```
Add the dependency
```
:dependencies [[com.github.premacck/facilis "1.0.0"]]
```

### Usage

- First, create an activity with `BaseCardActivity()` as superClass and return your fragment container ViewGroup(generally an empty FrameLayout) in getFragmentContainer()
```
class SelectionActivity : BaseCardActivity() {

    override fun getFragmentContainer(): Int = R.id.fragmentContainer
}
```
- After that you can push fragments or popup using `pushFragment(BaseFragment)` or `pushPopup(BaseDialogFragment)` methods

- To create a card fragment:
  - Create a fragment class with BaseCardFragment() as superclass and override the following methods
    ```
    class PlainCardFragment : BaseCardFragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
                inflater.inflate(R.layout.fragment_plain_card, container, false)

        /**
         * Return the background layout (preferably BlurLayout) for effects during transition
         */
        override fun getBackgroundBlurLayout(): ViewGroup? = blurLayout

        /**
         * Return the Drag area that should be used for swipe gestures
         */
        override fun getDragView(): View? = dragArea

        /**
         * Return the root view (preferably a CardView) of the fragment
         */
        override fun getRootView(): ViewGroup? = rootCard

        /**
         * Return the Drag handle resource ID to toggle visibility during transition
         */
        override fun dragHandleId(): Int = R.id.drag_handle_image
    }
    ```
  - Add XML data to fragment's layout (refer to styles.xml for style templates):
    ```
    <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:id="@+id/root_layout"
        style="@style/MatchParentMatchParent"
        android:animateLayoutChanges="true">

        <io.alterac.blurkit.BlurLayout
            android:id="@+id/blurLayout"
            style="@style/BlurLayoutFullScreen" />

        <androidx.cardview.widget.CardView
            android:id="@+id/rootCard"
            style="@style/CardView.WrapWidthHeight"
            android:layout_centerInParent="true">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="center_horizontal"
                android:layout_marginTop="20dp"
                android:gravity="center"
                android:padding="14dp"
                android:text="@string/this_is_a_sample_plain_card_fragment"
                android:textColor="@android:color/white"
                android:textSize="16sp"
                android:textStyle="bold" />

            <TextView
                android:layout_width="280dp"
                android:layout_height="wrap_content"
                android:layout_gravity="center_horizontal"
                android:layout_marginTop="100dp"
                android:layout_marginBottom="100dp"
                android:gravity="center"
                android:padding="14dp"
                android:text="@string/you_can_swipe_down_from_anywhere_inside_to_dismiss_this_card"
                android:textColor="@android:color/white"
                android:textSize="14sp" />

            <ImageView
                android:id="@+id/drag_handle_image"
                style="@style/DragHandleLight"
                android:contentDescription="@string/drag_handle" />

        </androidx.cardview.widget.CardView>

        <View
            android:id="@+id/dragArea"
            style="@style/DragAreaFull"
            android:layout_alignStart="@+id/rootCard"
            android:layout_alignTop="@+id/rootCard"
            android:layout_alignEnd="@+id/rootCard"
            android:layout_alignBottom="@+id/rootCard" />

    </RelativeLayout>
    ```

- To create a popup:
  - Create a dialogFragment class with BaseBlurPopup() as superclass and override the following methods
  ```
  class SampleBlurPopup : BaseBlurPopup() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.popup_sample_blur, container, false)

    /**
     * Optional - override for customizing enter animation of the root view returned by [getRootView()]
     */
    override fun enterAnimation(): Int = enterAnim

    /**
     * Optional - override for customizing exit animation of the root view returned by [getRootView()]
     */
    override fun dismissAnimation(): Int = exitAnim

    /**
     * Return the background layout (preferably BlurLayout) for effects during transition, return null for transparent background and no effects
     */
    override fun getBlurLayout(): BlurLayout? = blurLayout

    /**
     * Return the Drag area that should be used for swipe gestures, return null for no drag gesture implementations
     */
    override fun getDragHandle(): View? = dragArea

    /**
     * Return the root view (preferably a CardView) of the fragment
     */
    override fun getRootView(): View? = rootCard

    /**
     * Return the background layout (preferably BlurLayout) for effects during transition, return null for transparent background and no effects
     */
    override fun getBackgroundLayout(): ViewGroup? = blurLayout
  }
  ```
  - Add XML data to fragment's layout (refer to styles.xml for style templates):
  ```
  <?xml version="1.0" encoding="utf-8"?>
  <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
      style="@style/MatchParentMatchParent">

      <io.alterac.blurkit.BlurLayout
          android:id="@+id/blurLayout"
          style="@style/BlurLayoutFullScreen" />

      <androidx.cardview.widget.CardView
          android:id="@+id/rootCard"
          style="@style/CardView.WrapHeight"
          android:layout_gravity="center">

          <ScrollView style="@style/MatchParentMatchParent">

              <RelativeLayout
                  style="@style/MatchParentMatchParent"
                  android:orientation="vertical">

                  <ImageView
                      android:id="@+id/popupThumbnail"
                      android:layout_width="match_parent"
                      android:layout_height="wrap_content"
                      android:contentDescription="@string/thumbnail"
                      android:minHeight="100dp"
                      android:scaleType="centerCrop" />

                  <TextView
                      android:id="@+id/popupHeader"
                      style="@style/MatchParentWrapContent"
                      android:layout_below="@+id/popupThumbnail"
                      android:layout_gravity="center_horizontal"
                      android:gravity="center"
                      android:padding="20dp"
                      android:text="@string/this_is_a_sample_blur_popup"
                      android:textColor="@color/grey"
                      android:textSize="16sp"
                      android:textStyle="bold" />

                  <TextView
                      android:id="@+id/popupInfo"
                      style="@style/MatchParentWrapContent"
                      android:layout_below="@+id/popupHeader"
                      android:layout_gravity="center_horizontal"
                      android:gravity="center"
                      android:paddingStart="20dp"
                      android:paddingTop="10dp"
                      android:paddingEnd="20dp"
                      android:paddingBottom="10dp"
                      android:text="@string/blur_popup_implementation"
                      android:textColor="@color/grey_0_7"
                      android:textSize="14sp" />

              </RelativeLayout>

          </ScrollView>

          <ImageView
              android:id="@+id/drag_handle_image"
              style="@style/DragHandleLight"
              android:contentDescription="@string/drag_handle" />

          <View
              android:id="@+id/dragArea"
              style="@style/DragAreaWrap" />

      </androidx.cardview.widget.CardView>

  </FrameLayout>
  ```

- Refer to the [wiki pages](https://github.com/premacck/facilis/wiki) for more implementations

## Built With

* [Picasso](http://square.github.io/picasso/) - Image loading
* [Deck ViewPager](https://github.com/bloderxd/deck) - Customized ViewPager
* [Kotlin](https://kotlinlang.org/) - For less code
* [Anko](https://github.com/Kotlin/anko) - For easier life

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags).

## Authors

* **Prem Suman** - *A-Z* - [GitHub](https://github.com/premacck)

See also the list of [contributors](https://github.com/premacck/facilis/contributors) who participated in this project.

## Acknowledgments

* Hat tip to anyone whose code was used
