# Animation performance

Scope: BrownianMotion, CircleGridScale, CircleAndDotGrid and SnowFlakes. Preserve history length, update cadence, random sampling, particle order, colors/alpha, stroke widths and animation geometry. No resolution reduction or new dependencies.

Plan (2026-10-31):

1. Freeze the existing Brownian/circle renderers in JVM-only reference fixtures. Add seeded Brownian raster comparisons and circle comparisons across dimensions, phases and translucent colors. Lock snow particle movement, lifetime and stable removal before optimization.
2. Replace Brownian snapshot-list shifts with primitive ring buffers and one redraw state, retaining the 2,000-point age order and per-segment fade.
3. Cull only fully invisible circles/dot orbits with a conservative antialias/stroke margin, preserving original traversal and transforms. Reuse per-frame colors and strokes.
4. Compact snow particles in place and cache their unchanged fall-speed expression. Keep the original per-frame displacement and spawn timing.
5. Run regression tests on JVM/Wasm, raster comparisons, isolated before/after update/draw/allocation measurements, both build targets, and live smoke checks.

## Verification and measurements

The pre-optimization baseline regression suite passed before storage/culling changes. JVM raster comparisons use the same Skia runtime, seeded random motion and frozen reference renderers. They compare every pixel for 96 circle cases (four viewport shapes, six phases, opaque/translucent foreground), Brownian history/fade across two full 2,000-point wraps, and stable snow removal. Common tests lock ring age order/reset and the existing snow motion/lifetime rules. Snapshot observation also checks that primitive Brownian storage still invalidates the canvas.

Standalone JVM measurements on this machine: three batches after warmup, median of the per-operation batch means. Allocation is the JVM thread's allocated heap bytes, not retained memory. Brownian starts with a varied full 2,000-point history; snow uses 500 particles without births/deaths to isolate update/compaction cost. Drawing uses a CPU raster surface at a fixed animation phase.

| Work | Before ms/op | After ms/op | Before / after KiB allocated per op |
| --- | ---: | ---: | ---: |
| Brownian update | 13.251061 | 0.000602 | 2053.531 / <0.001 |
| Snow update, 500 particles | 0.009499 | 0.007665 | 8.656 / 0.021 |
| CircleGridScale raster, 390px | 0.291141 | 0.211385 | 39.086 / 0.055 |
| CircleGridScale raster, 600px | 0.540957 | 0.505709 | 39.086 / 0.055 |
| CircleAndDotGrid raster, 390px | 1.400010 | 1.234753 | 42.273 / 0.055 |
| CircleAndDotGrid raster, 600px | 1.989849 | 1.859614 | 42.273 / 0.055 |

Reproduce on macOS with JDK 17 (no extra dependencies):

```sh
./gradlew jvmTest createDistributable
java -Dskiko.library.path="$PWD/build/compose/binaries/main/app/an-arts.app/Contents/app" \
  -cp 'build/classes/kotlin/jvm/test:build/classes/kotlin/jvm/main:build/compose/binaries/main/app/an-arts.app/Contents/app/*' \
  com.sumygg.anarts.performance.AnimationBenchmarkKt
```

The benchmark is a standalone diagnostic, with no fragile timing assertions in normal tests. Reference implementations are JVM test fixtures and do not ship in either application. Brownian's reference uses plain lists for fast image comparisons and the original snapshot lists for the update benchmark.

Final validation: 53 JVM tests and 49 Chrome Wasm tests passed with zero failures. `check`, `wasmJsBrowserDistribution` and `createDistributable` passed. Live Web checks covered all four affected artworks plus pause/reset; Desktop checks covered Brownian drawing and pause. The Web console reported no runtime errors during these checks.

These numbers are isolated CPU/update results, not browser or GPU FPS or a promise of equivalent end-to-end speedup. Circle raster costs still include unchanged pixel fill, so reduced draw submission/allocation has a smaller effect on total draw time. No resolution/quality reduction was used. RotatingSquares, global scheduling, clock cadence and SnowFlakes' frame-based motion remain outside this change.
