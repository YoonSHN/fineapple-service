import React from 'react';
import { ShoppingCart, Star, X } from 'lucide-react';
import { cn } from '@/lib/utils';
import { Product } from '@/types/product';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';

interface ProductRecommendationProps {
  isOpen: boolean;
  onClose: () => void;
  products: Product[];
}

const ProductRecommendation: React.FC<ProductRecommendationProps> = ({
  isOpen,
  onClose,
  products,
}) => {
  if (!isOpen || products.length === 0) return null;

  const handlePurchase = (productId: string, productName: string) => {
    console.log(`Purchasing product: ${productName} (ID: ${productId})`);
    alert(`${productName} 구매가 시작되었습니다.`);
  };

  return (
    <div
      className={cn(
        'fixed inset-0 bg-black/50 flex items-center justify-center z-50 transition-opacity duration-300',
        isOpen ? 'opacity-100' : 'opacity-0 pointer-events-none'
      )}
    >
      <div
        className={cn(
          'bg-white rounded-2xl shadow-xl max-w-6xl w-full max-h-[90vh] overflow-auto transition-all duration-300',
          isOpen ? 'scale-100' : 'scale-95'
        )}
      >
        {/* 헤더 */}
        <div className="sticky top-0 flex items-center justify-between px-6 py-4 border-b border-gray-100 bg-white z-10">
          <h3 className="text-xl font-medium">추천 제품</h3>
          <button
            onClick={onClose}
            className="p-2 rounded-full hover:bg-gray-100 transition-colors"
            aria-label="Close recommendations"
          >
            <X className="h-5 w-5" />
          </button>
        </div>

        {/* 제품 카드 그리드 */}
        <div className="p-6 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
          {products.map((product) => (
            <Card key={product.id} className="flex flex-col w-full h-full">
              <CardHeader className="pb-4 flex-grow-0">
                <div className="w-full h-48 bg-gray-50 rounded-lg overflow-hidden mb-2 flex items-center justify-center">
                  {product.imageUrl ? (
                    <img
                      src={product.imageUrl}
                      alt={product.name}
                      className="w-full h-full object-cover"
                    />
                  ) : (
                    <div className="text-gray-400">이미지 없음</div>
                  )}
                </div>
                <CardTitle className="text-lg text-center">{product.name}</CardTitle>
                <div className="flex items-center justify-center text-yellow-500 mt-1">
                  {[1, 2, 3, 4, 5].map((_, i) => (
                    <Star
                      key={i}
                      className={`w-4 h-4 ${i < 4 ? 'fill-current' : ''}`}
                    />
                  ))}
                </div>
              </CardHeader>

              <CardContent className="flex-grow">
                <p className="text-gray-500 text-sm mb-3 text-center">{product.description}</p>
                <div className="space-y-1 text-center">
                  <h4 className="text-sm font-medium">주요 기능:</h4>
                  <ul className="text-sm space-y-1 flex flex-col items-center">
                      {product.features
                    .filter((feature) =>
                    ['칩셋', '디스플레이', '블루투스', '색상', '무게', '방수', '용도'].some((keyword) =>
                     feature.includes(keyword)
                    )
                    )
                    .map((feature, index) => (
                    <li key={index} className="flex items-center">
                     <span className="inline-block w-1.5 h-1.5 bg-black rounded-full mr-2" />
                     {feature}
                     </li>
                    ))}
                </ul>
                </div>
                <div className="mt-4 text-center">
                  <span className="text-xl font-semibold">{product.price}</span>
                </div>
              </CardContent>

              <CardFooter className="mt-auto">
                <Button
                  onClick={() => handlePurchase(product.id, product.name)}
                  className="w-full bg-black text-white hover:bg-gray-800 transition-colors"
                >
                  <ShoppingCart className="w-4 h-4 mr-2" />
                  구매하기
                </Button>
              </CardFooter>
            </Card>
          ))}
        </div>
      </div>
    </div>
  );
};

export default ProductRecommendation;
