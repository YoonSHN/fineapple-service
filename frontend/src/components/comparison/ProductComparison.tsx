import React from 'react';
import { X, ShoppingCart } from 'lucide-react';
import { cn } from '@/lib/utils';
import { Product } from '@/types/product';
import {
  Table,
  TableHeader,
  TableRow,
  TableHead,
  TableBody,
  TableCell
} from '@/components/ui/table';
import { Button } from '@/components/ui/button';

interface ProductComparisonProps {
  isOpen: boolean;
  onClose: () => void;
  products: Product[];
}

const allowedFeatureKeywords = [
  '칩셋',
  '디스플레이',
  '블루투스',
  '색상',
  '무게',
  '방수',
  '용도'
];

const parseFeatures = (features: string[]) => {
  const featureMap: Record<string, string> = {};
  features.forEach((feature) => {
    const [key, ...rest] = feature.split(':');
    if (!key || rest.length === 0) return;
    const value = rest.join(':').trim();
    if (allowedFeatureKeywords.includes(key.trim())) {
      featureMap[key.trim()] = value;
    }
  });
  return featureMap;
};

const compareValues = (key: string, values: string[]) => {
  if (key === '무게') {
    const weights = values.map((v) => parseFloat(v.replace(/[^\d.]/g, '')));
    const min = Math.min(...weights);
    return weights.map((w) => (w === min ? true : false));
  }
  if (key === '디스플레이') {
    const sizes = values.map((v) => parseFloat(v));
    const max = Math.max(...sizes);
    return sizes.map((s) => (s === max ? true : false));
  }
  if (key === '칩셋') {
    const rank = (v: string) =>
      v.includes('M2') ? 3 : v.includes('M1') ? 2 : 1;
    const scores = values.map(rank);
    const max = Math.max(...scores);
    return scores.map((s) => s === max);
  }
  return values.map(() => false);
};

const ProductComparison: React.FC<ProductComparisonProps> = ({
  isOpen,
  onClose,
  products
}) => {
  if (!isOpen || products.length === 0) return null;

  const productFeatureMaps = products.map((p) => parseFeatures(p.features));
  const allFeatureKeys = [
    ...new Set(productFeatureMaps.flatMap((f) => Object.keys(f)))
  ];

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
          'bg-white rounded-2xl shadow-xl max-w-5xl w-full max-h-[90vh] overflow-auto transition-all duration-300',
          isOpen ? 'scale-100' : 'scale-95'
        )}
      >
        <div className="sticky top-0 flex items-center justify-between px-6 py-4 border-b border-gray-100 bg-white">
          <h3 className="text-xl font-medium">제품 비교</h3>
          <button
            onClick={onClose}
            className="p-2 rounded-full hover:bg-gray-100 transition-colors"
            aria-label="Close comparison"
          >
            <X className="h-5 w-5" />
          </button>
        </div>

        <div className="p-6">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead className="w-1/3">특성</TableHead>
                {products.map((product) => (
                  <TableHead key={product.id}>{product.name}</TableHead>
                ))}
              </TableRow>
            </TableHeader>
            <TableBody>
              {/* 제품 이미지 */}
              <TableRow>
                <TableCell className="font-medium">제품 이미지</TableCell>
                {products.map((product) => (
                  <TableCell key={`${product.id}-image`} className="py-4">
                    <div className="flex justify-center">
                      {product.imageUrl ? (
                        <img
                          src={product.imageUrl}
                          alt={product.name}
                          className="w-32 h-32 object-contain rounded-xl border border-gray-200"
                        />
                      ) : (
                        <div className="w-32 h-32 bg-gray-100 rounded-xl border border-gray-200 flex items-center justify-center text-gray-400">
                          이미지 없음
                        </div>
                      )}
                    </div>
                  </TableCell>
                ))}
              </TableRow>

              {/* 가격 */}
              <TableRow>
                <TableCell className="font-medium">가격</TableCell>
                {products.map((product) => (
                  <TableCell key={`${product.id}-price`}>{product.price}</TableCell>
                ))}
              </TableRow>

              {/* 주요 기능 비교 */}
              {allFeatureKeys.map((key) => {
                const values = productFeatureMaps.map((map) => map[key] || '');
                const wins = compareValues(key, values);
                return (
                  <TableRow key={key}>
                    <TableCell className="font-medium">{key}</TableCell>
                    {values.map((value, i) => (
                      <TableCell
                        key={`${products[i].id}-${key}`}
                        className={wins[i] ? 'font-semibold text-green-600' : ''}
                      >
                        {value || '-'}
                      </TableCell>
                    ))}
                  </TableRow>
                );
              })}

              {/* 구매 버튼 */}
              <TableRow>
                <TableCell className="font-medium">구매</TableCell>
                {products.map((product) => (
                  <TableCell key={`${product.id}-purchase`}>
                    <Button
                      onClick={() => handlePurchase(product.id, product.name)}
                      className="bg-black text-white hover:bg-gray-800 transition-colors"
                      size="sm"
                    >
                      <ShoppingCart className="w-4 h-4 mr-1" />
                      구매하기
                    </Button>
                  </TableCell>
                ))}
              </TableRow>
            </TableBody>
          </Table>
        </div>
      </div>
    </div>
  );
};

export default ProductComparison;
