Android SlideShow Widget
========================

A set of widgets to create smooth slide shows with ease. The slide show components are fully 
customizable and are not limited to pictures, you can slide whatever you want to (texts, etc.).

## Demo

A demo of the widget is worth a thousand words. You can download it for free on Google Play.

<a href="https://play.google.com/store/apps/details?id=com.marvinlabs.widget.slideshow.demo">
  <img alt="Demo on Google Play"
         src="http://developer.android.com/images/brand/en_generic_rgb_wo_60.png" />
</a>

## Usage

### Including the library

The easiest way to get the library included in your project is by using Gradle. Simply add the 
following line to your dependencies block:

    dependencies {
        compile 'com.marvinlabs:android-slideshow-widget:0.4.+@aar'
    }
    
Of course, you can replace the version number by whichever version you need (you can have a look at 
this repository's tags to know which is the latest).

### Getting a slide show in your fragment/activity

To include a slide show in your layout, simply use the following XML code snippet:

    <com.marvinlabs.widget.slideshow.SlideShowView
        android:id="@+id/slideshow"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

You can then get the `SlideShowView` using the usual technique:

    slideShowView = (SlideShowView) findViewById(R.id.slideshow);

The next step is to create the adapter that will bind your slide data to actual views. This is a 
process very similar to what you do when working with the `ListView` widget. In this library, 
we have a few base adapters to help you create picture slide shows easily from images in your
application resources or from images on the Internet. The code would look like that:

    adapter = new ResourceBitmapAdapter(this, new int[]{
        R.raw.slide_01, R.raw.slide_02, R.raw.slide_03, R.raw.slide_04});

Once your activity is ready, or inside an event handler like a button click method, you can then 
start the slideshow:

    slideShowView.play();

## Customisable components

### SlideShowAdapter

This is the key component to bind your slide data with actual view widgets. Have you heard about 
`ListView` and `Adapter`? Well, this is exactly the same concept. 

To get you started with picture slide shows, we have created a few useful adapters:

  - `ResourceBitmapAdapter` will allow you to show a list of application resource drawables
  - `RemoteBitmapAdapter` will allow you to show a list of images that are loaded from the 
    Internet.
    
We also have a Picasso plugin library. That provides an adapter that works with Picasso instead of
AsyncTasks. To use it, simply add the corresponding dependency using gradle:

    dependencies {
        compile 'com.marvinlabs:android-slideshow-widget-picasso-plugin:0.4.+@aar'
    }
    
### SlideTransitionFactory

You want something else than just a fade between slides? Simply implement this interface to return 
whichever view animator you want. 

To make it easy for you, we already have a few default implementations that should cover most of 
your needs:
 
  - `FadeTransitionFactory` if you want your slides to fade out and in
  - `SlideAndZoomTransitionFactory` if you want your slides to slide ou left and then zoom in
  - `NoTransitionFactory`  if you want your slides to brutally show up

### PlayList

You want to change the order of your slides? You need your slides to have a variable duration? 
Well, that's the interface to implement to change that. 

To make it easy for you, we already have a few default implementations that should cover most of 
your needs:

  - `SequentialPlayList` will play slides in order with a common default duration
  - `RandomPlayList` will play slides in a random order with a common default duration

## About Vincent & MarvinLabs

I am a freelance developer located in Biarritz, France. You can 
[have a look at my website](http://vincentprat.info) to get to know me a little better. If you want 
to follow me, here are some links:

* [Follow me on Twitter](http://twitter.com/vpratfr)
* [Follow me on Google+](https://plus.google.com/+VincentPrat)
* [Follow me on Facebook](http://www.facebook.com/vpratfr)

MarvinLabs is my digital studio specialised in native mobile applications and web sites. You can 
[browse our website](http://www.marvinlabs.com) to get to know us a little better. If you want to 
get updates about our work, you can also:

* [Follow us on Twitter](http://twitter.com/marvinlabs)
* [Follow us on Google+](https://plus.google.com/+Marvinlabs)
* [Follow us on Facebook](http://www.facebook.com/studio.marvinlabs)

## Change log

### 0.4.0 (2014-06-03)

  - [new] added a Picasso plugin (an adapter to use Picasso to load remote images)

### 0.3.0 (2014-06-02)

  - [new] new transition factories
  - [new] playback control function (next, previous, pause, resume)
  - [new] widget can be configured from XML
  - [fix] issue #2

### 0.2.0 (2014-06-01)

  - [new] a RandomPlayList implementation
  - [new] listener for slide show events
  - [new] listener for slide click event (similar to ListView's item click listener) (Thanks 
    Arasthel)
  - [fix] Some additional inline documentation
  
### 0.1.0 (2014-05-31)

  - First release (beta)
  - SlideShowView widget to play a slide show
  - SlideTransitionFactory to customize the transitions between slides (with 2 default 
    implementations)
  - PlayList to customize the order and duration of slides (with 1 default implementations)
  - SlideShowAdapter to bind slide data to actual view widgets (with 2 default implementations)
  - Demo application