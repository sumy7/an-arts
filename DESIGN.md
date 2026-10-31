# Design

## Source of truth
Status: Active. Date: 2026-10-30. Surfaces: Compose Desktop (JVM) and Web (Wasm).
Evidence: README.md; App.kt; ExplorerView.kt; ArtsView.kt; DynamicForm.kt; FormItem.kt; ArtsModel.kt; the 19 registered artworks; existing screenshots. Previous Obsidian-inspired workspace was reviewed before this change.
Reference: user's current shadcn direction supersedes the earlier Obsidian styling. Official sources: https://ui.shadcn.com/docs/theming, https://ui.shadcn.com/docs/components/base/button, https://ui.shadcn.com/docs/components/base/input, https://ui.shadcn.com/docs/components/base/popover, https://ui.shadcn.com/docs/components/base/slider, https://www.radix-ui.com/primitives/docs/components/radio-group. shadcn React components are ported to shared Compose behavior; no React runtime is added. The official component catalog has no standalone Color Picker; the custom picker composes its Popover/Input/Slider patterns.

## Brand
A quiet, precise creative tool. Neutral zinc surfaces, monochrome controls, crisp typography and artwork as the main visual. Avoid gradients, large colored app bars, decorative cards, artificial tabs, fake actions and marketing language.

## Product goals
Make finding an artwork, viewing it and editing its parameters obvious. Preserve all 19 artworks, all existing form bindings, RGBA selection, reset and animation playback on both platforms. Success: useful canvas space at desktop and mobile widths, discoverable controls, no overflowing panels and working keyboard navigation. Non-goals: accounts, cloud storage, export, plugins or a new drawing engine.

## Personas and jobs
Creative coders and curious viewers explore algorithms, compare visual effects, adjust colors and pause animation. Contexts include a resizable desktop window and a browser from 360px to wide desktop sizes.

## Information architecture
One workspace: navigation ribbon, searchable artwork library, central canvas, parameter inspector and statusbar. There are no pretend document tabs. Art metadata provides a Chinese display title, original English name and concise explanation. Search matches both names and explanations; all/animated/static filters combine with the search. The inspector always describes the selected artwork and exposes its existing controls.

## Design principles
Prioritize the artwork. Separate navigation from editing. Show state in text as well as icons. Use compact chrome with comfortable targets. Keep actions reversible and preserve parameters when opening or closing panels or changing viewport size. Selecting a different artwork resets its configuration as before; a dedicated reset action reinitializes the current artwork. Theme changes affect chrome, not artwork colors.

## Visual language
Default light palette: background/popover #FFFFFF, sidebar #FAFAFA, text/primary #09090B/#18181B, muted #71717A, accent #F4F4F5, border #E4E4E7, ring #A1A1AA. Dark: background/popover #09090B, sidebar #18181B, foreground #FAFAFA, muted #A1A1AA, accent #27272A, border #3F3F46, ring #71717A. Tokens live in WorkspaceTheme.kt. Shared sans-serif typography; 14sp body, 12sp metadata, 18–20sp artwork title; no custom fonts/dependencies. Spacing uses 4/8/12/16/24dp. Controls use 6dp corners, popovers 8dp, 1dp borders, subtle overlay shadow and clear 2dp focus rings. Inputs/buttons are 36dp high; compact layout preserves generous spacing. Use existing vector icons. No Material ripple or decorative motion; only selected artwork animates, with pause.

## Components
WorkspaceTheme owns neutral palette/typography/shapes. ShadcnControls owns Default/Outline/Secondary/Ghost buttons, outlined inputs, focus/hover/disabled/invalid states and slender sliders with round outlined thumbs. ExplorerView owns Sidebar, Tabs, toolbar, Tooltip and responsive Sheet. ArtsView owns canvas rendering/frame timing. DynamicForm/FormItem own labels, inputs, radio groups and color triggers. ColorEditorHost positions a same-owner Popover at the color trigger, collision-adjusted to the viewport; compact windows use a Sheet. ColorPicker owns the custom saturation/value plane, hue/alpha sliders, HEX/RGBA inputs and presets. Valid changes update immediately; Escape/outside/close keep edits and return focus to the trigger if it still exists; breakpoint changes may dismiss the popover and use the workspace focus fallback when the old trigger is disposed. Desktop Popover is nonmodal: background remains accessible and Tab can exit; compact Sheet contains focus. No Dialog/Popup owner is introduced due to existing Web accessibility disposal issues. ArtsModel, ArtworkInfo and WorkspaceState retain selection/playback/search/panel responsibilities.

## Accessibility
Target WCAG 2.2 AA for chrome contrast and focus feedback; do not claim a formal audit. Label every icon button and slider. Keyboard focus must be visible. Cmd/Ctrl+B toggles library, Cmd/Ctrl+K opens and focuses search, Cmd/Ctrl+, toggles inspector, Escape dismisses the topmost popover or compact sheet and restores focus. Space toggles playback only when the canvas is focused, never while editing text. Preserve system shortcuts outside handled commands. Focus rings are visible on buttons, inputs, sliders, tabs and radios. Sliders support arrows, PageUp/PageDown and Home/End. Tabs/radio groups support arrows and a single Tab stop. The color plane has a keyboard alternative through saturation/brightness and channel controls. Color choices display hex values as well as swatches. Do not autoplay an artwork after the user pauses it merely because a panel or theme changes; reset preserves playback state.

## Responsive behavior
Wide >=1100dp: 48dp ribbon, 232dp library, flexible canvas and 280dp inspector, independently collapsible. Medium 760–1099dp: ribbon and optional library, canvas; inspector opens as a dismissible overlay. Compact <760dp: compact topbar, canvas and one modal side panel at a time, max panel width viewport minus 32dp. Side panels stay in the same Compose rendering owner, with background semantics hidden, focus constrained to the panel, Escape/backdrop dismissal and focus returned to the canvas. Never force horizontal page scrolling. Canvas offers a centered square and an adaptive view. All parameter lists and the library scroll internally.

## Interaction states
Selected artwork has a persistent highlight. Search includes a clear action and a real result count. No results shows a concise explanation and a clear-search action. Artworks without fields show an intentional empty state, while playback controls remain available for animated works. Reset is immediate and local. Color changes are live. Invalid HEX/channel text stays editable with an error and does not change the artwork; valid values retain alpha. Dismissing the color popover preserves its last valid edit. Static works show a static label instead of a disabled play button. Browser bootstrap/offline remains the existing loading behavior; do not add fake success or loading states.

## Content voice
Chinese interface labels, English artwork identifiers retained as useful metadata. Use short direct labels such as 作品库, 参数, 播放, 暂停, 重置, 专注模式. Explanations should describe the artwork rather than framework internals. No emoji badges or invented feature claims.

## Implementation constraints
Retain Kotlin/Compose Multiplatform versions and JVM/Wasm targets. No new dependencies. Keep algorithm changes outside this UI task. Use a shared Material theme and existing lifecycle. Baseline tests must pass before edits. Add focused tests for metadata/filtering, playback/reset and responsive panel state, then run JVM/browser tests, check, Web distribution and desktop compile/package. Verify actual wide/compact browser UI and desktop UI with screenshots. Save evidence under ignored build/reports/previews.

Implementation plan: preserve workspace layout/state and artwork behavior; replace Material button/input/slider/radio presentation with shared neutral primitives; replace transactional RGBA dialog with anchored live color picker; verify keyboard, invalid input, alpha and viewport collision handling. Remove obsolete modal editor UI rather than retain parallel controls. Preserve drawing/configuration behavior with existing regression tests plus new state tests. Reviewer passes are separate from implementation. No masking error fallbacks or alternate drawing paths will be introduced.

## Open questions
None blocking. Default light appearance follows the shadcn neutral scaffold; appearance and panel preferences are session-local. Cross-session preference persistence and saved presets are deferred.
