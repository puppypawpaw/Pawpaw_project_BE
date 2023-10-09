#!/bin/zsh
aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin 520511553477.dkr.ecr.ap-northeast-2.amazonaws.com