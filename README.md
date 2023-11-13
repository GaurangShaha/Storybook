# Storybook
A library that helps you create a list of components by scanning compose functions that are marked with @Preview annotation and not private. It will help to show your components in isolation.

## Version [![](https://jitpack.io/v/GaurangShaha/Storybook.svg)](https://jitpack.io/#GaurangShaha/Storybook)

## Integration
In your setting.gradle add an entry for Jitapack,
```
dependencyResolutionManagement {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}
```

Now in your build.gradle add the following lines,
```
plugins {
    id "com.google.devtools.ksp" version "ksp_version" 
}

dependencies {
    ksp "com.github.GaurangShaha:Storybook:1.0.2"
}
```
The KSP version is dependent on the Kotlin compiler that you are using. You can find the KSP versions [here](https://github.com/google/ksp/releases).

## Use case
The following are the functionalities supported by the library,
- Creates a list of components by scanning functions marked with @Preview annotation and are not private. The name of the component will be derived from the name of a function.
- The user can search the component in a list with its name.
- On tapping any item in the list user can see the component rendered.

It supports @Preview annotation out-of-the-box. If you have created any custom annotation for preview purposes, you can pass its fully qualified name as an argument to KSP in your build.gradle file.
```
ksp {
    arg("extendedPreviews", "com.flea.market.ui.preview.FleaMarketPreviews")
}
```
If you have multiple annotations for preview, pass them as a comma-separated string.

The generated compose function will have the name as the name of the module appended by Storybook. Eg. a module with the name ui-component will have generated a compose function with name UiComponentStorybook.

Following is the recording from the samples

![Storybook demo](https://github.com/GaurangShaha/Storybook/assets/20768606/d2fef0c6-41a2-4d31-88d8-09842ef6a476)
