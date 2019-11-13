# DrawerXArrowDrawable
A simple Drawable for your drawer that easily animates between a hamburger, arrow, and X

![Alt text](.github/.readmeImages/demo.gif)

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