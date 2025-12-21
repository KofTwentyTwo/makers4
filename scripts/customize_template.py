#!/usr/bin/env python3
"""
Customize the QQQ template:
 - Set your Java package
 - Set groupId / artifactId
 - Set the app metadata name/label
 - Move source folders to the new package path

This is intended for one-time use right after cloning.
"""

import re
import shutil
from pathlib import Path


def prompt(msg: str, default: str) -> str:
    val = input(f"{msg} [{default}]: ").strip()
    return val or default


def replace_in_files(files, replacements):
    for file in files:
        text = file.read_text(encoding="utf-8")
        changed = text
        for old, new in replacements.items():
            changed = changed.replace(old, new)
        if changed != text:
            file.write_text(changed, encoding="utf-8")


def main():
    repo = Path(__file__).resolve().parents[1]
    pom = repo / "pom.xml"
    if not pom.exists():
        raise SystemExit("Run from the template repo; pom.xml not found.")

    old_pkg = "com.example.orders"
    old_pkg_path = Path(*old_pkg.split("."))
    new_pkg = prompt("Java package", "com.example.myapp")
    new_pkg_path = Path(*new_pkg.split("."))

    group_id = prompt("Maven groupId", "com.example")
    artifact_id = prompt("Maven artifactId", "my-qqq-app")

    app_name = prompt("App name (metadata id)", "myApp")
    app_label = prompt("App label", "My App")

    # Move Java sources to the new package path
    for src_root in ["src/main/java", "src/test/java"]:
        src_dir = repo / src_root / old_pkg_path
        if src_dir.exists():
            dst_dir = repo / src_root / new_pkg_path
            dst_dir.parent.mkdir(parents=True, exist_ok=True)
            shutil.move(str(src_dir), str(dst_dir))
            # remove any empty parent dirs of the old path
            try:
                (repo / src_root / "com/example").rmdir()
            except Exception:
                pass

    java_files = list((repo / "src").rglob("*.java"))
    doc_files = [repo / "README.md", repo / "QUICK_START.md"]

    replacements = {
        f"package {old_pkg}": f"package {new_pkg}",
        f"import {old_pkg}.": f"import {new_pkg}.",
        "com.example.orders": new_pkg,
        "ordersApp": app_name,
        ">Orders<": f">{app_label}<",
        "\"Orders\"": f"\"{app_label}\"",
        "<groupId>com.example</groupId>": f"<groupId>{group_id}</groupId>",
        "<artifactId>new-qqq-application-template</artifactId>": f"<artifactId>{artifact_id}</artifactId>",
    }

    replace_in_files(java_files + doc_files + [pom], replacements)

    # Clean up potential leftover empty directories from the old package path
    for base in (repo / "src/main/java", repo / "src/test/java"):
        old_dir = base / "com" / "example" / "orders"
        if old_dir.exists():
            shutil.rmtree(old_dir, ignore_errors=True)

    print("\nTemplate customization complete.")
    print(f"- Package set to {new_pkg}")
    print(f"- groupId: {group_id}")
    print(f"- artifactId: {artifact_id}")
    print(f"- App id: {app_name}, label: {app_label}")
    print("Re-run `mvn clean package -DskipTests` after reviewing changes.")


if __name__ == "__main__":
    main()

