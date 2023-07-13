package com.github.nitrico.lastadapter_sample.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.nitrico.lastadapter.Holder;
import com.github.nitrico.lastadapter.ItemType;
import com.github.nitrico.lastadapter.LastAdapter;
import com.github.nitrico.lastadapter.TypeHandler;
import com.github.nitrico.lastadapter_sample.data.Car;
import com.github.nitrico.lastadapter_sample.data.Data;
import com.github.nitrico.lastadapter_sample.data.Header;
import com.github.nitrico.lastadapter_sample.data.Person;
import com.github.nitrico.lastadapter_sample.data.Point;
import com.github.nitrico.lastadapter_sample.data.StableData;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import info.jukov.leastadapter_sample.BR;
import info.jukov.leastadapter_sample.R;
import info.jukov.leastadapter_sample.databinding.ItemCarBinding;
import info.jukov.leastadapter_sample.databinding.ItemHeaderBinding;
import info.jukov.leastadapter_sample.databinding.ItemHeaderFirstBinding;
import info.jukov.leastadapter_sample.databinding.ItemPersonBinding;
import info.jukov.leastadapter_sample.databinding.ItemPointBinding;

public class JavaListFragment extends ListFragment {

    public static final String TAG = JavaListFragment.class.getSimpleName();

    private final ItemType<ItemHeaderFirstBinding> typeHeaderFirst = new ItemType<>(R.layout.item_header_first) {
        @Override
        public void onCreate(@NotNull final Holder<ItemHeaderFirstBinding> holder) {
            holder.itemView.setOnClickListener(v -> toast(getContext(), "Clicked " + holder.getBinding().getItem() + " at #" + holder.getAdapterPosition()));
        }

        @Override
        public void onBind(@NotNull Holder<ItemHeaderFirstBinding> holder) {
            Log.d(TAG, "Bound " + holder.getBinding().getItem() + " at #" + holder.getAdapterPosition());
        }

        @Override
        public void onRecycle(@NotNull Holder<ItemHeaderFirstBinding> holder) {
            Log.d(TAG, "Recycled " + holder.getBinding().getItem() + " at #" + holder.getAdapterPosition());
        }
    };

    private final ItemType<ItemHeaderBinding> typeHeader = new ItemType<>(R.layout.item_header) {
        @Override
        public void onCreate(final @NotNull Holder<ItemHeaderBinding> holder) {
            holder.itemView.setOnClickListener(v -> toast(getContext(), "Clicked " + holder.getBinding().getItem() + " at #" + holder.getAdapterPosition()));
        }

        @Override
        public void onBind(@NotNull Holder<ItemHeaderBinding> holder) {
            Log.d(TAG, "Bound " + holder.getBinding().getItem() + " at #" + holder.getAdapterPosition());
        }

        @Override
        public void onRecycle(@NotNull Holder<ItemHeaderBinding> holder) {
            Log.d(TAG, "Recycled " + holder.getBinding().getItem() + " at #" + holder.getAdapterPosition());
        }
    };

    private final ItemType<ItemPointBinding> typePoint = new ItemType<>(R.layout.item_point) {
        @Override
        public void onCreate(final @NotNull Holder<ItemPointBinding> holder) {
            holder.itemView.setOnClickListener(v -> toast(getContext(), "Clicked " + holder.getBinding().getItem() + " at #" + holder.getAdapterPosition()));
        }

        @Override
        public void onBind(@NotNull Holder<ItemPointBinding> holder) {
            Log.d(TAG, "Bound " + holder.getBinding().getItem() + " at #" + holder.getAdapterPosition());
        }

        @Override
        public void onRecycle(@NotNull Holder<ItemPointBinding> holder) {
            Log.d(TAG, "Recycled " + holder.getBinding().getItem() + " at #" + holder.getAdapterPosition());
        }
    };

    private final ItemType<ItemCarBinding> typeCar = new ItemType<>(R.layout.item_car) {
        @Override
        public void onCreate(final @NotNull Holder<ItemCarBinding> holder) {
            holder.itemView.setOnClickListener(v -> toast(getContext(), "Clicked " + holder.getBinding().getItem() + " at #" + holder.getAdapterPosition()));
        }

        @Override
        public void onBind(@NotNull Holder<ItemCarBinding> holder) {
            Log.d(TAG, "Bound " + holder.getBinding().getItem() + " at #" + holder.getAdapterPosition());
        }

        @Override
        public void onRecycle(@NotNull Holder<ItemCarBinding> holder) {
            Log.d(TAG, "Recycled " + holder.getBinding().getItem() + " at #" + holder.getAdapterPosition());
        }
    };

    private final ItemType<ItemPersonBinding> typePerson = new ItemType<>(R.layout.item_person) {
        @Override
        public void onCreate(final @NotNull Holder<ItemPersonBinding> holder) {
            holder.itemView.setOnClickListener(v -> toast(getContext(), "Clicked " + holder.getBinding().getItem() + " at #" + holder.getAdapterPosition()));
        }

        @Override
        public void onBind(@NotNull Holder<ItemPersonBinding> holder) {
            Log.d(TAG, "Bound " + holder.getBinding().getItem() + " at #" + holder.getAdapterPosition());
        }

        @Override
        public void onRecycle(@NotNull Holder<ItemPersonBinding> holder) {
            Log.d(TAG, "Recycled " + holder.getBinding().getItem() + " at #" + holder.getAdapterPosition());
        }
    };


    public JavaListFragment() { }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Object> items = Data.INSTANCE.getItems();
        boolean stableIds = items == StableData.INSTANCE.getItems();

        setTypeHandlerAdapter(items, stableIds);
    }


    private void setTypeHandlerAdapter(List<Object> items, boolean stableIds) {
        new LastAdapter(items, BR.item, stableIds).handler((TypeHandler) (item, position) -> {
            if (item instanceof Header) return position == 0 ? typeHeaderFirst : typeHeader;
            else if (item instanceof Point) return typePoint;
            else if (item instanceof Person) return typePerson;
            else if (item instanceof Car) return typeCar;
            return null;
        }).into(list);
    }

    private static void toast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
