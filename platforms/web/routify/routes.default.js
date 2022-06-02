import _default_focus_svelte from "../src/routes/focus.svelte";
import _default_goal_svelte from "../src/routes/goal.svelte";
import _default_index_svelte from "../src/routes/index.svelte";
import _default_goaledit_svelte from "../src/routes/goaledit.svelte";
import _default_goaledit2_svelte from "../src/routes/goaledit2.svelte";
import _default_presetEdit_svelte from "../src/routes/presetEdit.svelte";
import _default_session_svelte from "../src/routes/session.svelte";
import _default_summary_svelte from "../src/routes/summary.svelte";

export default {
  meta: {},
  id: "_default",
  file: {
    path: "src/routes",
    dir: "src",
    base: "routes",
    ext: "",
    name: "routes",
  },
  rootName: "default",
  routifyDir: import.meta.url,
  children: [
    {
      meta: {},
      id: "_default_focus_svelte",
      name: "focus",
      module: () => new Promise(resolve => resolve(_default_focus_svelte)),
      file: { path: "src/routes/focus.svelte", dir: "src/routes", base: "focus.svelte", ext: ".svelte", name: "focus" },
      children: [],
    },
    {
      meta: {},
      id: "_default_goal_svelte",
      name: "goal",
      module: () => new Promise(resolve => resolve(_default_goal_svelte)),
      file: { path: "src/routes/goal.svelte", dir: "src/routes", base: "goal.svelte", ext: ".svelte", name: "goal" },
      children: [],
    },
    {
      meta: {},
      id: "_default_goaledit_svelte",
      name: "goaledit",
      module: () => new Promise(resolve => resolve(_default_goaledit_svelte)),
      file: {
        path: "src/routes/goaledit.svelte",
        dir: "src/routes",
        base: "goaledit.svelte",
        ext: ".svelte",
        name: "goaledit",
      },
      children: [],
    },
    {
      meta: {},
      id: "_default_goaledit2_svelte",
      name: "goaledit2",
      module: () => new Promise(resolve => resolve(_default_goaledit2_svelte)),
      file: {
        path: "src/routes/goaledit2.svelte",
        dir: "src/routes",
        base: "goaledit2.svelte",
        ext: ".svelte",
        name: "goaledit2",
      },
      children: [],
    },
    {
      meta: {},
      id: "_default_index_svelte",
      name: "index",
      module: () => new Promise(resolve => resolve(_default_index_svelte)),
      file: {
        path: "src/routes/index.svelte",
        dir: "src/routes",
        base: "index.svelte",
        ext: ".svelte",
        name: "index",
      },
      children: [],
    },
    {
      meta: {},
      id: "_default_presetEdit_svelte",
      name: "presetEdit",
      module: () => new Promise(resolve => resolve(_default_presetEdit_svelte)),
      file: {
        path: "src/routes/presetEdit.svelte",
        dir: "src/routes",
        base: "presetEdit.svelte",
        ext: ".svelte",
        name: "presetEdit",
      },
      children: [],
    },
    {
      meta: {},
      id: "_default_session_svelte",
      name: "session",
      module: () => new Promise(resolve => resolve(_default_session_svelte)),
      file: {
        path: "src/routes/session.svelte",
        dir: "src/routes",
        base: "session.svelte",
        ext: ".svelte",
        name: "session",
      },
      children: [],
    },
    {
      meta: {},
      id: "_default_summary_svelte",
      name: "summary",
      module: () => new Promise(resolve => resolve(_default_summary_svelte)),
      file: {
        path: "src/routes/summary.svelte",
        dir: "src/routes",
        base: "summary.svelte",
        ext: ".svelte",
        name: "summary",
      },
      children: [],
    },
  ],
};
