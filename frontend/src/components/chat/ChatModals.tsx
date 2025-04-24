import React from 'react';
import ProductComparison from '@/components/comparison/ProductComparison';
import ProductRecommendation from '@/components/recommendation/ProductRecommendation';
import { Product } from '@/types/product';

interface ChatModalsProps {
  showComparison: boolean;
  showRecommendation: boolean;
  onCloseComparison: () => void;
  onCloseRecommendation: () => void;
  comparisonProducts: Product[];
  recommendationProducts: Product[];
}

const ChatModals: React.FC<ChatModalsProps> = ({
  showComparison,
  showRecommendation,
  onCloseComparison,
  onCloseRecommendation,
  comparisonProducts,
  recommendationProducts,
}) => {
  return (
    <>
      {showComparison && (
        <ProductComparison
          isOpen={showComparison}
          onClose={onCloseComparison}
          products={comparisonProducts}
        />
      )}

      {showRecommendation && (
        <ProductRecommendation
          isOpen={showRecommendation}
          onClose={onCloseRecommendation}
          products={recommendationProducts}
        />
      )}
    </>
  );
};

export default ChatModals;
