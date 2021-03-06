/*
 * MIT License
 *
 * Copyright (c) 2018 Pavel Annin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ru.annin.gallerytestassignment.presentation.gallery.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import ru.annin.gallerytestassignment.data.entity.Photo;
import ru.annin.gallerytestassignment.data.repository.common.NetworkState;
import ru.annin.gallerytestassignment.data.repository.common.PagedListing;
import ru.annin.gallerytestassignment.domain.GalleryUseCase;

/**
 * @author Pavel Annin.
 */
public class GalleryListViewModel extends ViewModel {

    private final GalleryUseCase useCase;
    private final LiveData<PagedListing<Photo>> listingLiveData;

    public GalleryListViewModel(@NonNull GalleryUseCase useCase) {
        this.useCase =useCase;
        listingLiveData = Transformations.map(useCase.getListingLiveData(), input -> input);
    }

    public void loadGallery(@NonNull String query) { useCase.fetchPhoto(query); }

    public void refresh() {
        final PagedListing<Photo> listing = listingLiveData.getValue();
        if (listing != null) {
            listing.makeRefresh();
        }
    }

    public void retryRequest() {
        final PagedListing<Photo> listing = listingLiveData.getValue();
        if (listing != null) {
            listing.makeRetry();
        }
    }

    @NonNull
    public LiveData<PagedList<Photo>> getPagedListLiveData() {
        return Transformations.switchMap(listingLiveData, PagedListing::getPagedList);
    }

    @NonNull
    public LiveData<NetworkState> getInitialStateLiveData() {
        return Transformations.switchMap(listingLiveData, PagedListing::getInitialState);
    }

    @NonNull
    public LiveData<NetworkState> getNetworkStateLiveData() {
        return Transformations.switchMap(listingLiveData, PagedListing::getNetworkState);
    }
}