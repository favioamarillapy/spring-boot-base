package com.py.konecta.base.service;

import com.py.konecta.base.dto.CustomResponse;
import com.py.konecta.base.dto.PageDTO;

public interface GenericService<T> {

    CustomResponse<PageDTO<T>> findAll(String filtros, int cantidad, int pagina, String orderBy, String orderDir);

    CustomResponse<T> findById(Long id);

    CustomResponse<T> create(T entity);

    CustomResponse<T> update(Long id, T entity);

    CustomResponse<T> activar(Long id);

    CustomResponse<T> desactivar(Long id);

    CustomResponse<Void> delete(Long id);
}
