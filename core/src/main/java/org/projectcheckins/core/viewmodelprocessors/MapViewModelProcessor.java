package org.projectcheckins.core.viewmodelprocessors;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.model.ViewModelProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class MapViewModelProcessor implements ViewModelProcessor<Map<String, Object>> {
    @Override
    public void process(@NonNull HttpRequest<?> request, @NonNull ModelAndView<Map<String, Object>> modelAndView) {
        populateModelAndView(request, modelAndView, this::populateModel);
    }

    private void populateModelAndView(@NonNull HttpRequest<?> request, ModelAndView<Map<String, Object>> modelAndView, BiConsumer<HttpRequest<?>, Map<String, Object>> viewModelConsumer) {
        Map<String, Object> viewModel = modelAndView.getModel().orElseGet(() -> {
            final HashMap<String, Object> newModel = new HashMap<>(1);
            modelAndView.setModel(newModel);
            return newModel;
        });
        try {
            viewModelConsumer.accept(request, viewModel);
        } catch (UnsupportedOperationException ex) {
            final HashMap<String, Object> modifiableModel = new HashMap<>(viewModel);
            populateModel(request, modifiableModel);
            modelAndView.setModel(modifiableModel);
        }
    }

    protected void populateModel(@NonNull HttpRequest<?> request, @NonNull Map<String, Object> viewModel) {}
}
