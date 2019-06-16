.file "tests/codeGenOfMe/if_03.dcf"
.text
.section .rodata
.LC0:
.string "hello world\n"
.LC1:
.string "yes the world\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $48, %rsp
movq $10,-41(%rbp)
movq $30,-33(%rbp)
movq $-40,-25(%rbp)
movq $14,-17(%rbp)
movq $25,-9(%rbp)
movb $1,-1(%rbp)
movq -41(%rbp),%rax
cmpq $10,%rax
jg .L1
movq -33(%rbp),%rax
cmpq $40,%rax
jge .L1
.L2:
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L3
.L1:
movq -41(%rbp),%rax
movq -33(%rbp),%r10
cmpq %r10,%rax
jge .L4
jmp .L2
.L4:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L3:
leave
ret

