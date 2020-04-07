#!/bin/bash
script_name='wiapack.main.kts'
script_dst='/opt/wiapack'

link_name='wiapack'
link_dst='/usr/local/bin'

mkdir "$script_dst" 2> /dev/null
cp "$(dirname "$0")/$script_name" "$script_dst/$script_name"

if [[ -f "$link_dst/$link_name" ]]; then
  rm "$link_dst/$link_name"
fi
ln -s "$script_dst/$script_name" "$link_dst/$link_name"
