.file "tests/codeGenOfMe/func_01.dcf"
.text
.section .rodata
.LC0:
.string "hello world\n"
.text
.globl hello
.type hello, @function
hello:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
movq $0,%rax
call hello
movq $0,%rax
leave
ret

