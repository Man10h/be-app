package com.Man10h.social_network_app.repository;

import com.Man10h.social_network_app.model.entity.PostEntity;
import com.Man10h.social_network_app.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, String> {

    @Query(value = """
    SELECT DISTINCT p FROM PostEntity p
    LEFT JOIN FETCH p.imageEntityList
    WHERE p.id = :id 
""")
    Optional<PostEntity> getPostsWithImage(@Param("id") String id);

    @Query(value = """
    SELECT DISTINCT p FROM PostEntity p
    LEFT JOIN FETCH p.commentEntityList
    WHERE p.id = :id 
""")
    Optional<PostEntity> getPostsWithComments(@Param("id") String id);

    @Query(
            value = """
    SELECT DISTINCT p FROM PostEntity p
    JOIN p.userEntity u
    WHERE u.id IN (
      SELECT fe.followerId
      FROM FollowerEntity fe
      WHERE fe.userEntity.id = :id
    )
  """,
            countQuery = """
    SELECT COUNT(p.id) FROM PostEntity p
    WHERE p.userEntity.id IN (
      SELECT fe.followerId
      FROM FollowerEntity fe
      WHERE fe.userEntity.id = :id
    )
  """
    )
    Page<PostEntity> getPosts(@Param("id") String userId, Pageable pageable);


    Page<PostEntity> findByUserEntity_Id(String userId, Pageable pageable);


    @Query(value = """
    SELECT DISTINCT p FROM PostEntity p
    LEFT JOIN FETCH p.imageEntityList
    WHERE p.id = :id 
""")
    Page<PostEntity> getAllPosts(Pageable pageable);
}
