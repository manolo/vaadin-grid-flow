/*
 * Copyright 2000-2020 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.vaadin.flow.component.grid.dataview;

import java.util.stream.Stream;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.AbstractDataView;
import com.vaadin.flow.data.provider.DataCommunicator;
import com.vaadin.flow.data.provider.DataProvider;

public class GridDataViewImpl<T> extends AbstractDataView<T>
        implements GridDataView<T> {

    private DataCommunicator<T> dataCommunicator;

    public GridDataViewImpl(DataCommunicator<T> dataCommunicator,
            Grid<T> grid) {
        super(dataCommunicator::getDataProvider, grid);
        this.dataCommunicator = dataCommunicator;
    }

    @Override
    public Stream<T> getItems() {
        return dataCommunicator.getDataProvider()
                .fetch(dataCommunicator.buildQuery(0, Integer.MAX_VALUE));
    }

    @Override
    public int getSize() {
        return dataCommunicator.getDataSize();
    }

    @Override
    public boolean contains(T item) {
        final DataProvider<T, ?> dataProvider = dataCommunicator
                .getDataProvider();
        final Object itemIdentifier = dataProvider.getId(item);
        return getItems()
                .anyMatch(i -> itemIdentifier.equals(dataProvider.getId(i)));
    }

    @Override
    protected Class<?> getSupportedDataProviderType() {
        return DataProvider.class;
    }

    @Override
    public T getItemOnRow(int rowIndex) {
        final int dataSize = getSize();
        if (rowIndex < 0 || rowIndex >= dataSize) {
            throw new IndexOutOfBoundsException(String.format(
                    "Given index %d is outside of the accepted range '0 - %d'",
                    rowIndex, dataSize - 1));
        }
        return getItems().skip(rowIndex).findFirst().orElse(null);
    }
}