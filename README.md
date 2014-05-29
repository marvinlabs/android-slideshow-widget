Android SlideShow Widgets
=========================

A set of widgets to create smooth slide shows with ease. The slide show components are fully 
customizable and are not limited to pictures, you can slide whatever you want to (texts, etc.).

## Components

### SlideShowView

This is our widget to display the slide show. That widget offers convenient defaults to play a nice 
slide show. You will simply need to give it an adapter to describe the slides and how to display 
them.

### SlideShowAdapter

This is the key component to bind your slide data with actual view widgets. Have you heard about 
`ListView` and `Adapter`? Well, this is exactly the same concept. 

To get you started with picture slide shows, we have created a few useful adapters:

  - `SimpleImageResourceAdapter` will allow you to show a list of application resource drawables
  - `RemoteImageResourceAdapter` will allow you to show a list of images that are loaded from the 
    Internet.
   
### SlideTransitionFactory

You want something else than just a fade between slides? Simply implement this interface to return 
whichever view animator you want. 

To make it easy for you, we already have a few default implementations that should cover most of 
your needs:
 
  - `FadeTransitionFactory` if you want your slides to fade
  - `NoTransitionFactory`  if you want your slides to brutally show up

### PlayList

You want to change the order of your slides? You need your slides to have a variable duration? 
Well, that's the interface to implement to change that. 

To make it easy for you, we already have a few default implementations that should cover most of 
your needs:

  - `SequentialPlayList` will play slides in order with a common default duration
  - `RandomPlayList` will play slides in a random order with a common default duration

## Usage

The easiest way to get the library included in your project is by using Gradle. Simply add the 
following line to your dependencies block:

    dependencies {
        compile 'com.marvinlabs:android-slideshow-widgets:1.0.+@aar'
    }
    
Of course, you can replace the version number by whichever version you need (you can have a look at 
this repository's tags to know which is the latest).

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

### 1.0.0 (2014-05-30)

  - SlideShowView widget to play a slide show
  - SlideTransitionFactory to customize the transitions between slides (with 2 default 
    implementations)
  - PlayList to customize the order and duration of slides (with 2 default implementations)
  - SlideShowAdapter to bind slide data to actual view widgets (with 2 default implementations)
  - Demo application