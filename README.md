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
* Minimum Android SDK: 14

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
    notifyChange = NotifyChange.DIFF_UTIL // Possible values are DIFF_UTIL, PLAIN, MANUAL
)
    // Map Header model to corresponding ViewBinding
    .map<Model.Header, LayoutHeaderBinding>(
        viewHolder = {
            // Specify the way for binding view. Choose one of the options:
            // Option 1 - provides item and ViewBinding
            onBindView { item, binding ->
                binding.headerText.text = item.text
                binding.root.setOnClickListener {
                    toast("Click on ${item.text}")
                }
            }
            // Option 2 - provides item, ViewBinding and current item position
            onBindView { item, binding, position ->
                binding.headerText.text = item.text
                binding.root.setOnClickListener {
                    toast("Click on ${item.text}")
                }
            }
            // Option 3 - provides item and Holder
            onBindView { item, holder ->
                holder.binding.headerText.text = item.text
                holder.binding.root.setOnClickListener {
                    toast("Click on ${item.text}")
                }
            }
            // Option 4 - provides item, Holder and current item position
            onBindView { item, holder, position ->
                holder.binding.headerText.text = item.text
                holder.binding.root.setOnClickListener {
                    toast("Click on ${item.text}")
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

All you need to do is set the flag `notifyChange = NotifyChange.DIFF_UTIL` in constructor, and either implement the `StableId` interface for your Items, or call the `itemComparison()` method in a `map` function and check the items there.
There is also an optional method `contentComparison()`. By default LeastAdapter checks items content using the `equals()` method.

New items can be provided with the `setItems()` method.

With flag `notifyChange = NotifyChange.PLAIN`, LeastAdapter uses `notifyDataSetChanged()`.
With flag `notifyChange = NotifyChange.MANUAL`, LeastAdapter just set new items to internal list`.

## StableIds

Stable ids is also supported in LeastAdapter.

To use that, you need to set the flag `stableIds = true` in constructor, and either implement the `StableId` interface for your Items, or call the `getItemId()` method in a `map` function and check items there.

## R8 / Proguard

The rules are [already included](leastadapter/proguard-rules.txt) in the library and R8 will use them directly.

If you still use ProGuard, you need to add these rules manually.

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