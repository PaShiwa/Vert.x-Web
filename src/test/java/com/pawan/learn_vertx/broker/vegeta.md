# Vegeta Load Testing

Mac ->echo "GET http://localhost:8888/assets" | vegeta attack -workers=4 -max-workers=10 -duration=30s | tee results.bin | vegeta report
echo GET http://localhost:8888/assets | vegeta attack -workers=4 -max-workers=10 -duration=30s > results.bin && vegeta report results.bin
