# LeastAdapter

LeastAdapter is inspired by [LastAdapter](https://github.com/nitrico/LastAdapter ) lib for writing
RecyclerView adapters in convenient way.

* Based on Android [ViewBinding](https://developer.android.com/topic/libraries/view-binding)
* Written in [**Kotlin**](http://kotlinlang.org)
* No additional dependencies except RecyclerView itself
* No need to write the ViewHolders
* No need to modify your model classes
* No need to implement DiffUtil.Callback
* Supports multiple item view types
* Optional Callbacks/Listeners
* Minimum Android SDK: **14**

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
    implementation 'com.github.jukov:leastadapter:3.0.0'
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
            // Specify ViewBinding provider
            onCreateView { parent ->
                LayoutHeaderBinding.inflate(layoutInflater, parent, false)  
            }
            // Specify way for binding view
            onBindView { _, model, binding ->
                binding.headerText.text = model.text
                binding.root.setOnClickListener {
                    toast("Click on ${model.text}")
                }
            }
            // Specify stableId provider
            getItemId { model ->
                model.id.toLong()
            }
            // Specify item comparison for DiffUtil, similar to method DiffUtil.Callback.areItemsTheSame()
            itemComparison { old, new ->  
                old.id == new.id
            }
            // Specify item comparison for DiffUtil, similar to method DiffUtil.Callback.areContentsTheSame()
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