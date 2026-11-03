package org.route.repository;

import org.route.model.orderPromotion.Promotion;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class PromotionRepository {
   private final Map<String, Promotion> promotions=new HashMap<>();

   public Map<String,Promotion> findAll() {
       return promotions;
   }

   public Optional<Promotion> getPromotion(String promotionId) {
        return Optional.ofNullable(promotions.get(promotionId.toLowerCase()));
   }

   public void addPromotion(Promotion promotion,String promotionId) {
       promotions.put(promotionId.toLowerCase(),promotion);
   }


}
