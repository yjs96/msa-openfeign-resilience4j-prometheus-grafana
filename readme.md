# 1. Circuit Breaker 테스트 (연속 호출로 서킷 열기)
for i in {1..20}; do
curl -X POST http://localhost:8080/api/orders \
-H "Content-Type: application/json" \
-d '{"orderId":"123","productId":"ABC","quantity":2}'
echo ""
sleep 0.5
done

# 2. Rate Limiter 테스트 (빠른 연속 호출)
for i in {1..15}; do
curl -X POST http://localhost:8080/api/orders \
-H "Content-Type: application/json" \
-d '{"orderId":"123","productId":"ABC","quantity":2}'
echo ""
done

# 3. Bulkhead 테스트 (동시 다중 호출)
for i in {1..20}; do
curl -X POST http://localhost:8080/api/orders \
-H "Content-Type: application/json" \
-d '{"orderId":"123","productId":"ABC","quantity":2}' &
done