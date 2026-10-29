# Design

## Source of truth
Status: Active. Date: 2026-10-30. Surfaces: Compose Desktop (JVM) and Web (Wasm).
Evidence: README.md; App.kt; ExplorerView.kt; ArtsView.kt; DynamicForm.kt; FormItem.kt; ArtsModel.kt; the 19 registered artworks; existing screenshots. No previous design system was present.
Reference: the user's Obsidian direction, https://obsidian.md/help/workspace, https://obsidian.md/help/sidebar and https://docs.obsidian.md/Reference/CSS%20variables/Foundations/Colors. This is an art workspace inspired by its spatial hierarchy, not an Obsidian clone.

## Brand
A quiet, precise creative tool. Charcoal surfaces, restrained violet selection, crisp typography and artwork as the main visual. Avoid gradients, large colored app bars, decorative cards, artificial tabs, fake actions and marketing language.

## Product goals
Make finding an artwork, viewing it and editing its parameters obvious. Preserve all 19 artworks, all existing form bindings, RGBA selection, reset and animation playback on both platforms. Success: useful canvas space at desktop and mobile widths, discoverable controls, no overflowing panels and working keyboard navigation. Non-goals: accounts, cloud storage, export, plugins or a new drawing engine.

## Personas and jobs
Creative coders and curious viewers explore algorithms, compare visual effects, adjust colors and pause animation. Contexts include a resizable desktop window and a browser from 360px to wide desktop sizes.

## Information architecture
One workspace: navigation ribbon, searchable artwork library, central canvas, parameter inspector and statusbar. There are no pretend document tabs. Art metadata provides a Chinese display title, original English name and concise explanation. Search matches both names and explanations; all/animated/static filters combine with the search. The inspector always describes the selected artwork and exposes its existing controls.

## Design principles
Prioritize the artwork. Separate navigation from editing. Show state in text as well as icons. Use compact chrome with comfortable targets. Keep actions reversible and preserve parameters when opening or closing panels or changing viewport size. Selecting a different artwork resets its configuration as before; a dedicated reset action reinitializes the current artwork. Theme changes affect chrome, not artwork colors.

## Visual language
Default dark palette: background #1E1E20, sidebar #19191C, raised surface #252528, border #333338, primary text #ECECF0, secondary #A6A6B2, accent #B5A2F3. Light mode uses the same roles with warm neutral whites. Tokens live in WorkspaceTheme.kt. Shared sans-serif typography; 14sp body, 12sp metadata, 18–20sp artwork title; do not use custom fonts or add dependencies. Spacing uses 4/8/12/16/24dp. Corners 6–10dp, 1dp borders, no heavy shadows. Use existing vector icon infrastructure. No gratuitous motion; only the selected artwork animates, with an explicit pause action.

## Components
WorkspaceTheme owns palette, typography, shapes and component colors. ExplorerView owns the responsive shell, ribbon, search/library, inspector and statusbar. ArtsView owns canvas rendering/frame timing. DynamicForm/FormItem own stacked field groups, numeric values, full-width swatch controls, selectable algorithm rows and RGBA editing with confirm/cancel. ColorEditorHost keeps its modal in the same rendering owner, hides background semantics and uses Compose focus restoration when it closes. Hover hints are also in the shared rendering owner. ArtIcons owns small reusable vector assets. ArtsModel owns selection, playback and reset behavior. ArtworkInfo owns display/search metadata. WorkspaceState owns panel and focus-mode transitions.

## Accessibility
Target WCAG 2.2 AA for chrome contrast and focus feedback; do not claim a formal audit. Label every icon button and slider. Keyboard focus must be visible. Cmd/Ctrl+B toggles library, Cmd/Ctrl+K opens and focuses search, Cmd/Ctrl+, toggles inspector, Escape dismisses compact overlays. Space toggles playback only when the canvas is focused, never while editing text. Preserve system shortcuts outside handled commands. Touch controls aim for 44dp targets. Color choices display hex values as well as swatches. Do not autoplay an artwork after the user pauses it merely because a panel or theme changes; reset preserves playback state.

## Responsive behavior
Wide >=1100dp: 48dp ribbon, 232dp library, flexible canvas and 280dp inspector, independently collapsible. Medium 760–1099dp: ribbon and optional library, canvas; inspector opens as a dismissible overlay. Compact <760dp: compact topbar, canvas and one modal side panel at a time, max panel width viewport minus 32dp. Side panels stay in the same Compose rendering owner, with background semantics hidden, focus constrained to the panel, Escape/backdrop dismissal and focus returned to the canvas. Never force horizontal page scrolling. Canvas offers a centered square and an adaptive view. All parameter lists and the library scroll internally.

## Interaction states
Selected artwork has a persistent highlight. Search includes a clear action and a real result count. No results shows a concise explanation and a clear-search action. Artworks without fields show an intentional empty state, while playback controls remain available for animated works. Reset is immediate and local. Dialog cancellation leaves color unchanged. Static works show a static label instead of a disabled play button. Browser bootstrap/offline remains the existing loading behavior; do not add fake success or loading states.

## Content voice
Chinese interface labels, English artwork identifiers retained as useful metadata. Use short direct labels such as 作品库, 参数, 播放, 暂停, 重置, 专注模式. Explanations should describe the artwork rather than framework internals. No emoji badges or invented feature claims.

## Implementation constraints
Retain Kotlin/Compose Multiplatform versions and JVM/Wasm targets. No new dependencies. Keep algorithm changes outside this UI task. Use a shared Material theme and existing lifecycle. Baseline tests must pass before edits. Add focused tests for metadata/filtering, playback/reset and responsive panel state, then run JVM/browser tests, check, Web distribution and desktop compile/package. Verify actual wide/compact browser UI and desktop UI with screenshots. Save evidence under ignored build/reports/previews.

Cleanup plan: replace fixed two-column layout and default app bars; remove superseded hover/layout wrappers only if unused; consolidate field spacing and colors in existing components. Preserve drawing/configuration behavior with existing regression tests plus new state tests. Reviewer passes are separate from implementation. No masking error fallbacks or alternate drawing paths will be introduced.

## Open questions
None blocking. Default dark appearance follows the user's Obsidian reference; appearance and panel preferences are session-local. Cross-session preference persistence and saved presets are deferred.
