package com.py.konecta.base.repository;

import com.py.konecta.base.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

	Optional<Usuario> findByUsuario(String usuario);

	@Query(value = "select  " + "	*  " + "from  " + "	public.usuarios c  " + "where  " + "	greatest(  "
			+ "coalesce(c.fecha_creacion,  " + "	cast('1970-01-01' as TIMESTAMP)),  "
			+ "	coalesce(c.fecha_modificacion,  " + "	cast('1970-01-01' as TIMESTAMP)),  "
			+ "	coalesce(c.fecha_eliminacion,  " + "	cast('1970-01-01' as TIMESTAMP)))   "
			+ "> TO_TIMESTAMP(:fechaDesde/1000.0)  " + "order by  " + "	greatest(  " + "coalesce(c.fecha_creacion,  "
			+ "	cast('1970-01-01' as TIMESTAMP)),  " + "	coalesce(c.fecha_modificacion,  "
			+ "	cast('1970-01-01' as TIMESTAMP)),  " + "	coalesce(c.fecha_eliminacion,  "
			+ "	cast('1970-01-01' as TIMESTAMP))) asc  " + "limit :cantidad", nativeQuery = true)
	List<Usuario> sincronizar(@Param("fechaDesde") long fechaDesde, @Param("cantidad") int cantidad);

	@Query(value = "select  " + "	*  " + "from  " + "	public.usuarios c  " + "where c.rol = 'ROLE_CHOFER' and " + "	greatest(  "
            + "coalesce(c.fecha_creacion,  " + "	cast('1970-01-01' as TIMESTAMP)),  "
            + "	coalesce(c.fecha_modificacion,  " + "	cast('1970-01-01' as TIMESTAMP)),  "
            + "	coalesce(c.fecha_eliminacion,  " + "	cast('1970-01-01' as TIMESTAMP)))   "
            + "> TO_TIMESTAMP(:fechaDesde/1000.0)  " + "order by  " + "	greatest(  " + "coalesce(c.fecha_creacion,  "
            + "	cast('1970-01-01' as TIMESTAMP)),  " + "	coalesce(c.fecha_modificacion,  "
            + "	cast('1970-01-01' as TIMESTAMP)),  " + "	coalesce(c.fecha_eliminacion,  "
            + "	cast('1970-01-01' as TIMESTAMP))) asc  " + "limit :cantidad", nativeQuery = true)
	List<Usuario> sincronizarChofer(@Param("fechaDesde") long fechaDesde, @Param("cantidad") int cantidad);
}
