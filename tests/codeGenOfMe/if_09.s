.file "tests/codeGenOfMe/if_09.dcf"
.text
.section .rodata
.LC0:
.string "it is ok\n"
.LC1:
.string "it is wrong\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
movq $3,%rax
movq $4,%r10
cmpq %r10,%rax
jne .L1
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L2
.L1:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L2:
leave
ret

