# DrawerXArrowDrawable
A simple Drawable for your drawer that easily animates between a hamburger, arrow, and X

![Alt text](.github/.readmeImages/demo.gif)

## Purpose
The DrawerArrowDrawable class in Android doesn't include animations to an X.  It also requires you to use an ObjectAnimator to do the animations.  With DrawerXArrowDrawable, you get a hamburger, arrow, and X.  And you don't need to worry about ObjectAnimators.

## To add to project:

Be sure to add the jcenter repository

    repositories {
        jcenter()
    }

Then add the library to your dependencies

    implementation 'com.ober:drawerxarrowdrawable:0.1.2'
    
## Usage

        
        
``` kotlin
val drawerXArrowDrawable = DrawerXArrowDrawable(this, DrawerXArrowDrawable.Mode.DRAWER).apply {
    color = Color.WHITE
}
supportActionBar?.setHomeAsUpIndicator(drawerXArrowDrawable)
supportActionBar?.setDisplayHomeAsUpEnabled(true)
```