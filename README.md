# LeastAdapter

LeastAdapter is inspired by [LastAdapter](https://github.com/nitrico/LastAdapter) lib for writing
RecyclerView adapters in convenient way.

* Based on Android [ViewBinding](https://developer.android.com/topic/libraries/view-binding)
* Written in [Kotlin](http://kotlinlang.org)
* No additional dependencies except RecyclerView itself
* No need to write the ViewHolders
* No need to modify your model classes
* No need to implement DiffUtil.Callback
* Supports multiple item view types
* Optional Callbacks/Listeners
* Minimum Android SDK: 21

## Setup

### Gradle

**build.gradle** (project)

```gradle
buildscript {
    ...
}

allprojects {
    repositories {
    
        ...
        
        // Add jitpack repository
        maven {
            url "https://jitpack.io"  
        }
    }
}
```

**build.gradle** (app)

```gradle

android {
    ...
}
  
dependencies {
    // Add LeastAdapter dependency
    implementation 'com.github.jukov:leastadapter:2'
}
```

**Don't forget to add RecyclerView dependency and configure ViewBinding!**

## Usage

```kotlin

adapter = LeastAdapter(
    items = items, // Optional way to provide initial data to provider
    stableIds = true, // Set true if you want to provide stableIds to RecyclerView
    diffUtil = true  // Set true if you want to use DiffUtil for smooth updates
)
    // Map Header model to corresponding ViewBinding
    .map<Model.Header, LayoutHeaderBinding>(
        viewHolder = {
            // Specify way for binding view
            onBindView { model, binding, position ->
                binding.headerText.text = model.text
                binding.root.setOnClickListener {
                    toast("Click on ${model.text}")
                }
            }
            // Optional stableId provider
            getItemId { model ->
                model.id.toLong()
            }
            // Optional item comparison for DiffUtil, similar to method DiffUtil.Callback.areItemsTheSame()
            itemComparison { old, new ->  
                old.id == new.id
            }
            // Optional content comparison for DiffUtil, similar to method DiffUtil.Callback.areContentsTheSame()
            contentComparison { old, new ->
                old == new
            }
        }    )
    .map<Model.Item, LayoutItemBinding>(
        viewHolder = {
            ...
        }
    )
    .into(recyclerView)

```

## DiffUtil

[DiffUtil](https://developer.android.com/reference/androidx/recyclerview/widget/DiffUtil) is internally supported in LeastAdapter. There is no need to implement [ItemCallback](https://developer.android.com/reference/androidx/recyclerview/widget/DiffUtil.ItemCallback) to use it.

All you need to do is set the flag `diffUtil = true` in constructor, and either implement the `StableId` interface for your Items, or call the `itemComparison()` method in a `map` function and check the items there.
There is also an optional method `contentComparison()`. By default LeastAdapter checks items content using the `equals()` method.

New items can be provided with the `setItems()` method.

With flag `diffUtil = false`, LeastAdapter uses plain old `notifyDataSetChanged()`.

## StableIds

Stable ids is also supported in LeastAdapter.

To use that, you need to set the flag `stableIds = true` in constructor, and either implement the `StableId` interface for your Items, or call the `getItemId()` method in a `map` function and check items there.

## License

```txt
Copyright 2023 Alexandr Zhukov 

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```